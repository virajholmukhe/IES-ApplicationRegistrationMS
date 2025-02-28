package com.ies.ApplicationRegistrationMS.service;

import com.ies.ApplicationRegistrationMS.dto.CitizenApplicationDTO;
import com.ies.ApplicationRegistrationMS.exception.ApplicationRegistrationMSException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplicationRegistrationService {
    public CitizenApplicationDTO createApplication(CitizenApplicationDTO citizenApplicationDTO, String jwt) throws ApplicationRegistrationMSException;
    public List<CitizenApplicationDTO> getApplications(String jwt) throws ApplicationRegistrationMSException;
    public Boolean updatePlanSelection(String caseNumber, String planId,String planName, String jwt) throws ApplicationRegistrationMSException;
    public CitizenApplicationDTO getApplication(String caseNumber, String jwt) throws ApplicationRegistrationMSException;
}
