package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScpiSimulationMapper {


    @Mapping(target = "simulationId", source = "scpiSimulation.scpiSimulationId.simulationId")
    @Mapping(target = "scpiId", source = "scpiSimulation.scpiSimulationId.scpiId")
    ScpiSimulationDToOut toDTO(ScpiSimulation scpiSimulation);
    List<ScpiSimulationDToOut> toDTO(List<ScpiSimulation> scpiSimulations);

    @Mappings({
            @Mapping(target = "scpiSimulationId", expression = "java(new ScpiSimulationId(simulationDTOIn.getSimulationId(), simulationDTOIn.getScpiId()))")
    })
    ScpiSimulation toEntity(ScpiSimulationInDTO simulationDTOIn);


}