package fr.formationacademy.scpiinvestplusapi.model.dto.requests;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestorRequest {

    @Email(regexp = EMAIL_PATTERN, message = INVALID_EMAIL)
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Min(value = 0, message = "Annual income must be positive")
    private Integer annualIncome;

    @Pattern(regexp = PHONE_PATTERN, message = INVALID_PHONE)
    private String phoneNumber;

    @NotBlank(message = "Marital status is required")
    private String maritalStatus;

    @Pattern(regexp = NUMBER_PATTERN, message = INVALID_NUMBER)
    private String numberOfChildren;
}
