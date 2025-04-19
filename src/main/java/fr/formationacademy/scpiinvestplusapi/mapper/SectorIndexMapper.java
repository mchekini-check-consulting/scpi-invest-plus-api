package fr.formationacademy.scpiinvestplusapi.mapper;


import fr.formationacademy.scpiinvestplusapi.dto.SectorIndexDto;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorIndexMapper {

    @Mapping(source = "id.name", target = "name")
    @Mapping(source = "sectorPercentage", target = "sectorPercentage")
    SectorIndexDto toSectorIndexDto(Sector sector);

    List<SectorIndexDto> toSectorIndexDtoList(List<Sector> sectors);

}
