package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScpiDtoOut {
    private Integer id;
    private String name;
    private Integer minimumSubscription;
    private LocationDtoOut location;
    private SectorDtoOut sector;
    private StatYearDtoOut statYear;
}
