package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring", uses = { ScpiSimulationMapper.class})
public interface SimulationMapper {
    @Mapping(target = "investorEmail", source = "investor.email")
    @Mapping(target =  "monthlyIncome", source ="simulation" , qualifiedByName = "getMonthlyIncome")
    @Mapping(target =  "totalInvestment", source ="simulation" , qualifiedByName = "getTotalInvestment")
    SimulationDToOut toDTO(Simulation simulation);

    Simulation toEntity(SimulationInDTO simulationDTOIn);

    List<SimulationDToOut> toDTO(List<Simulation> simulations);

    @Named("getMonthlyIncome")
    default Double getMonthlyIncome(Simulation simulation) {
        if(simulation.getScpiSimulations() != null && !simulation.getScpiSimulations().isEmpty()) {
            return simulation.getScpiSimulations().stream()
                    .map(scpiSimulation -> {
                        BigDecimal rate = scpiSimulation.getScpi().getStatYears().get(0).getDistributionRate();
                        BigDecimal invested = scpiSimulation.getRising();

                        return invested.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .doubleValue();
        }
        return 0d;
    }

    @Named("getTotalInvestment")
    default Double getTotalInvestment(Simulation simulation) {
        if(simulation.getScpiSimulations() != null && !simulation.getScpiSimulations().isEmpty()) {
               return simulation.getScpiSimulations().stream()
                    .map(ScpiSimulation::getRising)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .doubleValue();
        }
        return 0d;
    }
}
