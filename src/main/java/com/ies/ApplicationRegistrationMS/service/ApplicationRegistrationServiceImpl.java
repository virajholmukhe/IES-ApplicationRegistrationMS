package com.ies.ApplicationRegistrationMS.service;

import com.ies.ApplicationRegistrationMS.dto.CitizenApplicationDTO;
import com.ies.ApplicationRegistrationMS.entity.CitizenApplicationEntity;
import com.ies.ApplicationRegistrationMS.exception.ApplicationRegistrationMSException;
import com.ies.ApplicationRegistrationMS.exception.ExceptionConstants;
import com.ies.ApplicationRegistrationMS.repository.CitizenApplicationRepository;
import com.ies.ApplicationRegistrationMS.utils.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:messages.properties")
public class ApplicationRegistrationServiceImpl implements ApplicationRegistrationService {

    @Value("${aadharService.validateUser.api}")
    private String Aadhar_API_URL;

    @Autowired
    private CitizenApplicationRepository citizenApplicationRepository;

    @Autowired
    Environment environment;

    @Autowired
    JwtService jwtService;

    @Override
    public CitizenApplicationDTO createApplication(CitizenApplicationDTO citizenApplicationDTO, String jwt) throws ApplicationRegistrationMSException {

        try {
            jwtService.validateToken(jwt);
        } catch (Exception e) {
            throw new ApplicationRegistrationMSException(e.getMessage());
        }
        String userId = jwtService.getUserId(jwt);
        String userType = jwtService.getUserType(jwt);
        List<CitizenApplicationEntity> citizenApplicationEntityList = citizenApplicationRepository.findByCreatedBy(userId);
        if(!citizenApplicationEntityList.isEmpty() && userType.equalsIgnoreCase("USER")){
            throw new ApplicationRegistrationMSException(ExceptionConstants.USER_ALREADY_CREATED_APPLICATION.toString());
        }

        //check for email is already  exists
        String AadharNumber = citizenApplicationDTO.getCitizenAadhar();
        CitizenApplicationEntity citizenApplicationEntityCheck = citizenApplicationRepository.findByCitizenEmail(citizenApplicationDTO.getCitizenEmail());
        if(citizenApplicationEntityCheck != null){
            throw new ApplicationRegistrationMSException(ExceptionConstants.EMAIL_ALREADY_USED.toString());
        }

        //check for aadhar number is valid or not
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> response = restTemplate.getForEntity(Aadhar_API_URL + AadharNumber, Boolean.class);
        System.out.println(response.getBody());
        try{
            Boolean.TRUE.equals(response.getBody());
            CitizenApplicationEntity citizenApplicationEntity = new CitizenApplicationEntity();
            BeanUtils.copyProperties(citizenApplicationDTO, citizenApplicationEntity);
            CitizenApplicationEntity citizenApplicationEntityRespones = citizenApplicationRepository.save(citizenApplicationEntity);
            CitizenApplicationDTO citizenApplicationDTOResponse = new CitizenApplicationDTO();
            BeanUtils.copyProperties(citizenApplicationEntityRespones, citizenApplicationDTOResponse);
            return citizenApplicationDTOResponse;
        }catch (Exception e){
            throw new ApplicationRegistrationMSException(ExceptionConstants.INVALID_AADHAR_NUMBER.toString());
        }
    }

    @Override
    public List<CitizenApplicationDTO> getApplications(String jwt) throws ApplicationRegistrationMSException {
        try {
            jwtService.validateToken(jwt);
        } catch (Exception e) {
            throw new ApplicationRegistrationMSException(e.getMessage());
        }
        String userId = jwtService.getUserId(jwt);
        String userType = jwtService.getUserType(jwt);
        List<CitizenApplicationDTO> citizenApplicationDTOList = new ArrayList<>();
        List<CitizenApplicationEntity> citizenApplicationEntityList = new ArrayList<>();
        if(userType.equalsIgnoreCase("ADMIN")) {
            citizenApplicationEntityList = citizenApplicationRepository.findAll();
            for (CitizenApplicationEntity citizenApplicationEntity : citizenApplicationEntityList){
                CitizenApplicationDTO citizenApplicationDTO = new CitizenApplicationDTO();
                BeanUtils.copyProperties(citizenApplicationEntity, citizenApplicationDTO);
                citizenApplicationDTOList.add(citizenApplicationDTO);
            }
        }else{
            citizenApplicationEntityList = citizenApplicationRepository.findByCreatedBy(userId);
            for (CitizenApplicationEntity citizenApplicationEntity : citizenApplicationEntityList){
                CitizenApplicationDTO citizenApplicationDTO = new CitizenApplicationDTO();
                BeanUtils.copyProperties(citizenApplicationEntity, citizenApplicationDTO);
                citizenApplicationDTOList.add(citizenApplicationDTO);
            }
        }
        return citizenApplicationDTOList;
    }

    @Override
    public Boolean updatePlanSelection(String caseNumber, String planId, String planName, String jwt) throws ApplicationRegistrationMSException {
        try {
            jwtService.validateToken(jwt);
        } catch (Exception e) {
            throw new ApplicationRegistrationMSException(e.getMessage());
        }
        Optional<CitizenApplicationEntity> citizenApplicationEntity = citizenApplicationRepository.findById(caseNumber);
        if(citizenApplicationEntity.isPresent()
                && citizenApplicationEntity.get().getCreatedBy().equalsIgnoreCase(jwtService.getUserId(jwt))
        ){
            citizenApplicationEntity.get().setPlanId(planId);
            citizenApplicationEntity.get().setPlanName(planName);
            citizenApplicationRepository.save( citizenApplicationEntity.get());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public CitizenApplicationDTO getApplication(String caseNumber, String jwt) throws ApplicationRegistrationMSException {
        Optional<CitizenApplicationEntity> citizenApplicationEntity = citizenApplicationRepository.findById(caseNumber);
        if(citizenApplicationEntity.isPresent()){
            CitizenApplicationDTO citizenApplicationDTO = new CitizenApplicationDTO();
            BeanUtils.copyProperties(citizenApplicationEntity.get(), citizenApplicationDTO);
            return citizenApplicationDTO;
        }else{
            throw new ApplicationRegistrationMSException(ExceptionConstants.CASE_NUMBER_NOT_FOUND.toString());
        }
    }
}
