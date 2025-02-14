package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.entity.StatYeraId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatYearDtoOut {
    private StatYeraId yearStat;
    private Float distributionRate;
    private Float sharePrice;
    private Float reconstitutionValue;
}
