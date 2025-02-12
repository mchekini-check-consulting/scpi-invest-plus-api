package fr.formationacademy.scpiinvestplusapi.model.dto.responses;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestorResponse {
    private String email;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private Integer annualIncome;
    private String phoneNumber;
}

