package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Investor {

    @Id
    private String email;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private Integer annualIncome;
    private String phoneNumber;
    private String maritalStatus;
    private String numberOfChildren;
}
