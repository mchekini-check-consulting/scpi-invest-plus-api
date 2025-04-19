package fr.formationacademy.scpiinvestplusapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationIndexDto {

    private String country;
    private BigDecimal countryPercentage;

}
