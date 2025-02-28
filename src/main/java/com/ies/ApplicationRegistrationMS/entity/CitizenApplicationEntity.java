package com.ies.ApplicationRegistrationMS.entity;

import com.ies.ApplicationRegistrationMS.utils.CaseNumber;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "citizen_application")
@Data
public class CitizenApplicationEntity {

    @Id
    @CaseNumber
    private String caseNumber;
    private String planId;
    private String planName;
    private String citizenName;
    private String citizenEmail;
    private String citizenPhone;
    private String citizenGender;
    private LocalDate citizenDob;
    private String citizenAadhar;
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate updatedDate;
    private String createdBy;

}
