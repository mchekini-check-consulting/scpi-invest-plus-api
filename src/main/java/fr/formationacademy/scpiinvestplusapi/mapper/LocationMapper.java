package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDTO toDTO(LocationDTO location);
    Location toEntity(LocationDTO locationDTO);
}
