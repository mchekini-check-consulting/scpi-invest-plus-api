package fr.formationacademy.scpiinvestplusapi.model.dto.requests;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRequest {

    @NotBlank(message = "Country is required")
    private String country;

    @PositiveOrZero(message = "Country percentage must be non-negative")
    private Float countryPercentage;

    @NotNull(message = "SCPI ID is required")
    private Integer scpiId;
}
