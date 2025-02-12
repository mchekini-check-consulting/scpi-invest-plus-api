package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);
    LocationDtoOut locationToLocationDtoOut(Location location);
    List<LocationDtoOut> locationsToLocationsOut(List<Location> locations);
}
