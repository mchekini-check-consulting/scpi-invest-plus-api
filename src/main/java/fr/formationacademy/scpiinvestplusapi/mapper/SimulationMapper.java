package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.SectorDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Mapper(componentModel = "spring", uses = {ScpiSimulationMapper.class})
public interface SimulationMapper {
    @Mapping(target = "monthlyIncome", source = "simulation", qualifiedByName = "getMonthlyIncome")
    @Mapping(target = "totalInvestment", source = "simulation", qualifiedByName = "getTotalInvestment")
    @Mapping(target = "sectors", source = "simulation", qualifiedByName = "getGlobalSectorStatsForSimulation")
    @Mapping(target = "locations", source = "simulation", qualifiedByName = "getGlobalCountryStatForSimulation")
    SimulationDToOut toDTO(Simulation simulation);

    Simulation toEntity(SimulationInDTO simulationDTOIn);

    List<SimulationDToOut> toDTO(List<Simulation> simulations);

    @Named("getMonthlyIncome")
    default Double getMonthlyIncome(Simulation simulation) {
        if (simulation.getScpiSimulations() != null && !simulation.getScpiSimulations().isEmpty()) {
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
        if (simulation.getScpiSimulations() != null && !simulation.getScpiSimulations().isEmpty()) {
            return simulation.getScpiSimulations().stream()
                    .map(ScpiSimulation::getRising)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .doubleValue();
        }
        return 0d;
    }

    @Named("getGlobalSectorStatsForSimulation")
    @Mapping(target = "id", ignore = true)
    default List<SectorDtoOut> getGlobalSectorStatsForSimulation(Simulation simulation) {
        if (simulation.getScpiSimulations() == null || simulation.getScpiSimulations().isEmpty()) {
            return Collections.emptyList();
        }
        BigDecimal totalInvestment = BigDecimal.valueOf(getTotalInvestment(simulation));

        Map<String, BigDecimal> sectorInvestments = new HashMap<>();
        for (ScpiSimulation scpiSim : simulation.getScpiSimulations()) {
            BigDecimal scpiRising = scpiSim.getRising();

            for (Sector sector : scpiSim.getScpi().getSectors()) {
                String sectorName = sector.getId().getName();
                BigDecimal sectorPercentage = sector.getSectorPercentage();
                BigDecimal investedAmount = scpiRising.multiply(sectorPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                sectorInvestments.put(sectorName, sectorInvestments.getOrDefault(sectorName, BigDecimal.ZERO).add(investedAmount));
            }
        }
        List<SectorDtoOut> sectorDtos = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : sectorInvestments.entrySet()) {
            BigDecimal percentage = entry.getValue().multiply(BigDecimal.valueOf(100))
                    .divide(totalInvestment, 2, RoundingMode.HALF_UP);
            sectorDtos.add(new SectorDtoOut(new SectorId(simulation.getScpiSimulations().get(0).getScpi().getId(), entry.getKey()), percentage));
        }

        return sectorDtos;
    }

    @Named("getGlobalCountryStatForSimulation")

    default List<LocationDtoOut> getGlobalCountryStatForSimulation(Simulation simulation) {
        if (simulation.getScpiSimulations() != null && !simulation.getScpiSimulations().isEmpty()) {
            BigDecimal totalInvestesment = BigDecimal.valueOf(getTotalInvestment(simulation));
            Map<String, BigDecimal> countryInvestments = new HashMap<>();
            for (ScpiSimulation scpiSim : simulation.getScpiSimulations()) {
                BigDecimal scpiRising = scpiSim.getRising();
                for (Location location : scpiSim.getScpi().getLocations()) {
                    String countryName = location.getId().getCountry();
                    BigDecimal countryPercentatge = location.getCountryPercentage();
                    BigDecimal amountInvestment = scpiRising.multiply(countryPercentatge).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    countryInvestments.put(countryName, countryInvestments.getOrDefault(countryName, BigDecimal.ZERO).add(amountInvestment));
                }
            }

            List<LocationDtoOut> countryDtos = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : countryInvestments.entrySet()) {
                BigDecimal percentage = entry.getValue().multiply(BigDecimal.valueOf(100)).divide(totalInvestesment, 2, RoundingMode.HALF_UP);
                countryDtos.add(LocationDtoOut.builder()
                        .id(LocationId.builder().scpiId(simulation.getScpiSimulations().get(0).getScpi().getId()).country(entry.getKey()).build())
                        .countryPercentage(percentage)
                        .build());

            }
            return countryDtos;
        }
        return Collections.emptyList();
    }

}