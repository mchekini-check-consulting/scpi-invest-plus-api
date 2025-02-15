package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatYearDTO {
    private Integer year;
    private Float distributionRate;
    private Float sharePrice;
    private Float reconstitutionValue;

    private Integer scpiId;
}