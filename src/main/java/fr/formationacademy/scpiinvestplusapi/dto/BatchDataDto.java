package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.entity.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchDataDto {
    private ScpiDto scpiDto;
    private Scpi scpi;
    private List<Location> locations;
    private List<Sector> sectors;
    private List<StatYear> statYears;
}

