package fr.formationacademy.scpiinvestplusapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRequest {

    @NotBlank(message = "Country is required")
    private String country;

    @PositiveOrZero(message = "Country percentage must be non-negative")
    private BigDecimal countryPercentage;

    @NotNull(message = "SCPI ID is required")
    private Integer scpiId;
}
