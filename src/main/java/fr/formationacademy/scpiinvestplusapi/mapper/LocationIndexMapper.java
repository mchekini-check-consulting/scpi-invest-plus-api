package fr.formationacademy.scpiinvestplusapi.mapper;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.dto.LocationIndexDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationIndexMapper {


    @Mapping(source = "id.country", target = "country")
    @Mapping(source = "countryPercentage", target = "countryPercentage")
    LocationIndexDto toLocationIndexDto(Location location);

    List<LocationIndexDto> toLocationIndexDtoList(List<Location> locations);


}
