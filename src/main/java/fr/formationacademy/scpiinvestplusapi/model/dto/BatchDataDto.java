package fr.formationacademy.scpiinvestplusapi.model.dto;

import fr.formationacademy.scpiinvestplusapi.model.dto.requests.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.model.entiry.*;
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
    private Investor investor;
    private Scpi scpi;
    private List<Location> locations;
    private List<Sector> sectors;
    private List<StatYear> statYears;
}

