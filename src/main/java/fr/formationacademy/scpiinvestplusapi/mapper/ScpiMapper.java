package fr.formationacademy.scpiinvestplusapi.mapper;


import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScpiMapper {

    ScpiDTO toDTO(Scpi scpi);
    Scpi toEntity(ScpiDTO scpiDTO);
}
