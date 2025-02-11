package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScpiMapper {
    ScpiMapper INSTANCE = Mappers.getMapper(ScpiMapper.class);
    ScpiDtoOut scpiToScpiDtoOut(Scpi scpi);
    List<ScpiDtoOut> scpiToScpiDtoOut(List<Scpi> scpi);
}
