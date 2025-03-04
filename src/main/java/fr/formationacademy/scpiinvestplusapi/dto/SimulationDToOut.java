package fr.formationacademy.scpiinvestplusapi.dto;

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
    private Double monthlyIncome;
    private Double totalInvestment;
    private List<ScpiSimulationDToOut> scpiSimulations;
}
