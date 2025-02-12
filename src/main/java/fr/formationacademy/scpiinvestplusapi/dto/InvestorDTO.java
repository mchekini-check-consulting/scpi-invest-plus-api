package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvestorDTO {
    private String email;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private Integer annualIncome;
    private String phoneNumber;
    private String maritalStatus;
    private String numberOfChildren;
}
