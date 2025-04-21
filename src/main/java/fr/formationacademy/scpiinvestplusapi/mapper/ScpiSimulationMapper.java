package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScpiSimulationMapper {

    @Mapping(target = "scpiId", source = "scpiSimulation.scpiSimulationId.scpiId")
    @Mapping(target = "scpiName", source = "scpiSimulation.scpi.name")
    @Mapping(target = "locations", source = "scpiSimulation.scpi.locations")
    @Mapping(target = "sectors", source = "scpiSimulation.scpi.sectors")
    @Mapping(target = "statYears", source = "scpiSimulation.scpi.statYears")
    ScpiSimulationDToOut toDTO(ScpiSimulation scpiSimulation);

    List<ScpiSimulationDToOut> toDTO(List<ScpiSimulation> scpiSimulations);

    @Mapping(target = "scpiSimulationId", expression = "java(new ScpiSimulationId(simulation.getId(), simulationDTOIn.getScpiId()))")
    ScpiSimulation toEntity(ScpiSimulationInDTO simulationDTOIn, Simulation simulation);
}