package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDtoOut toDTO(Location location);
    Location toEntity(LocationDtoOut locationDTO);
}
