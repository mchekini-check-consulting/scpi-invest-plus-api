package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.StatYearDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatYearMapper {
    StatYearDtoOut toDTO(StatYear statYear);
}
