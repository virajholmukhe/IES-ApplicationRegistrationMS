package com.ies.ApplicationRegistrationMS.controller;

import com.ies.ApplicationRegistrationMS.dto.CitizenApplicationDTO;
import com.ies.ApplicationRegistrationMS.exception.ApplicationRegistrationMSException;
import com.ies.ApplicationRegistrationMS.service.ApplicationRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/application-registration")
public class ApplicationRegistrationController {

    @Autowired
    private ApplicationRegistrationService applicationRegistrationService;

    @Autowired
    Environment environment;

    @PostMapping(value = "/create-application")
    public ResponseEntity<CitizenApplicationDTO> createApplication(
            @RequestBody @Valid CitizenApplicationDTO citizenApplicationDTO,
            @RequestHeader("Authorization") String jwt
    ) throws ApplicationRegistrationMSException {
        CitizenApplicationDTO response = applicationRegistrationService.createApplication(citizenApplicationDTO, jwt);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-applications")
    public ResponseEntity<List<CitizenApplicationDTO>> getApplications(
            @RequestHeader("Authorization") String jwt
    ) throws ApplicationRegistrationMSException {
        return new ResponseEntity<>(applicationRegistrationService.getApplications(jwt), HttpStatus.OK);
    }

    @PutMapping(value = "/update-plan-selection")
    public ResponseEntity<Boolean> updatePlanSelection(
            @RequestParam String caseNumber,
            @RequestParam String planId,
            @RequestParam String planName,
            @RequestHeader("Authorization") String jwt
    ) throws ApplicationRegistrationMSException {
        return new ResponseEntity<>(applicationRegistrationService.updatePlanSelection(caseNumber, planId, planName, jwt), HttpStatus.OK);
    }

    @GetMapping(value = "/get-application/{caseNumber}")
    public ResponseEntity<CitizenApplicationDTO> getApplication(
            @PathVariable String caseNumber,
            @RequestHeader("Authorization") String jwt
    ) throws ApplicationRegistrationMSException {
        return new ResponseEntity<>(applicationRegistrationService.getApplication(caseNumber, jwt), HttpStatus.OK);
    }

}
