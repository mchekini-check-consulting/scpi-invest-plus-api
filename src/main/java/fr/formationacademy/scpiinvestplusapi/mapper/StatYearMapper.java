package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.StatYearDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StatYearMapper {
    StatYearMapper INSTANCE = Mappers.getMapper(StatYearMapper.class);
    StatYearDtoOut statYearToStatYearDtoOut(StatYear statYear);
    List<StatYearDtoOut> statYearToStatYearDtoOut(List<StatYear> statYear);
}
