package fr.formationacademy.scpiinvestplusapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatYearRequest {

    @Min(value = 1700, message = "Year must be realistic")
    private Integer yearStat;

    @PositiveOrZero(message = "Distribution rate must be non-negative")
    private Float distributionRate;

    @PositiveOrZero(message = "Share price must be non-negative")
    private Float sharePrice;

    @PositiveOrZero(message = "Reconstitution value must be non-negative")
    private Float reconstitutionValue;

    @NotNull(message = "SCPI ID is required")
    private Integer scpiId;
}

