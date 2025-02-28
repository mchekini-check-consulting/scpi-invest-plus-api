package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SimulationDToOut {
    private Integer id;
    private String name;
    private LocalDate simulationDate;
    private String investorEmail;
    private List<ScpiSimulationDToOut> scpiSimulations;
}
