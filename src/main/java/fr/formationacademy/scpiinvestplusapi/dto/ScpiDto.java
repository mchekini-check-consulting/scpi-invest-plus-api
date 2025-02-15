package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScpiDto {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 0, message = "Minimum subscription must be positive")
    private Integer minimumSubscription;

    @NotBlank(message = "Manager is required")
    private String manager;

    @PositiveOrZero(message = "Capitalization must be non-negative")
    private BigDecimal capitalization;

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

    @PositiveOrZero(message = "Cashback must be non-negative")
    private BigDecimal cashback;

    private String advertising;

    private String locations;
    private String sectors;
    //private String statYears;

    private List<StatYear> statYears;
}