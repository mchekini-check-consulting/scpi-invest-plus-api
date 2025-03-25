package fr.formationacademy.scpiinvestplusapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.*;

@Data
public class InvestorDTO {

    @Email(message = INVALID_EMAIL)
    @NotBlank(message = "Email is required")
    @Pattern(regexp = EMAIL_PATTERN, message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Integer annualIncome;

    @Pattern(regexp = PHONE_PATTERN, message = INVALID_PHONE)
    private String phoneNumber;

    @NotBlank(message = "Marital status is required")
    private String maritalStatus;

    @Pattern(regexp = NUMBER_PATTERN, message = INVALID_NUMBER)
    private String numberOfChildren;

    private List<InvestmentDto> investments;
}

