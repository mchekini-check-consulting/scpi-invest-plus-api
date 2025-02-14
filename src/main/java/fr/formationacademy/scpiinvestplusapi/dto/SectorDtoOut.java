package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.entity.SectorId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SectorDtoOut {
    private SectorId id;
    private Float sectorPercentage;
}
