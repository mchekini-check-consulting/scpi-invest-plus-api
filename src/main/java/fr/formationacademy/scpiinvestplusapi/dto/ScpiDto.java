package fr.formationacademy.scpiinvestplusapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScpiDto {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @DecimalMin(value = "0.0", message = "Distributed rate must be non-negative")
    private Float distributedRate;

    @DecimalMin(value = "0.0", message = "Share price must be non-negative")
    private Float sharePrice;

    @DecimalMin(value = "0.0", message = "Reconstitution value must be non-negative")
    private Float reconstitutionValue;

    @Min(value = 0, message = "Minimum subscription must be positive")
    private Integer minimumSubscription;

    @NotBlank(message = "Manager is required")
    private String manager;

    @PositiveOrZero(message = "Capitalization must be non-negative")
    private Long capitalization;

    @DecimalMin(value = "0.0", inclusive = true, message = "Subscription fees must be non-negative")
    private Float subscriptionFees;

    @DecimalMin(value = "0.0", inclusive = true, message = "Management costs must be non-negative")
    private Float managementCosts;

    @Min(value = 0, message = "Enjoyment delay must be non-negative")
    private Integer enjoymentDelay;

    @Pattern(regexp = IBAN_PATTERN, message = INVALID_IBAN)
    private String iban;

    @Pattern(regexp = BIC_PATTERN, message = INVALID_BIC)
    private String bic;

    private Boolean scheduledPayment;

    private String frequencyPayment;

    @PositiveOrZero(message = "Cashback must be non-negative")
    private Float cashback;

    private String advertising;

    private String locations;
    private String sectors;
    private Integer statYear;

    //private List<StatYear> statYears;
}