package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ScpiSimulationMapper.class})
public interface SimulationMapper {
    @Mapping(target = "investorEmail", source = "investor.email")

    SimulationDToOut toDTO(Simulation simulation);
    Simulation toEntity(SimulationInDTO simulationDTOIn);

    List<SimulationDToOut> toDTO(List<Simulation> simulations);

}
