package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.SectorDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SectorMapper {
    SectorMapper INSTANCE = Mappers.getMapper(SectorMapper.class);
    SectorDtoOut sectorToSectorDtoOut(Sector sector);
    List<SectorDtoOut> sectorToSectorDtoOut(List<Sector> sectors);
}
