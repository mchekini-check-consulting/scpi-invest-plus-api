package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.LocationRequest;
import fr.formationacademy.scpiinvestplusapi.dto.SectorRequest;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearRequest;
import fr.formationacademy.scpiinvestplusapi.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    default LocationRequest toRequest(Location location) {
        return new LocationRequest(
                location.getId().getCountry(),
                location.getCountryPercentage(),
                location.getId().getScpiId()
        );
    }

    default List<LocationRequest> toRequestLocationList(List<Location> locations) {
        return locations.stream()
                .map(this::toRequest)
                .toList();
    }

    default SectorRequest toRequest(Sector sector) {
        return new SectorRequest(
                sector.getId().getName(),
                sector.getSectorPercentage(),
                sector.getId().getScpiId()
        );
    }



    default List<SectorRequest> toRequestSectorList(List<Sector> sectors) {
        return sectors.stream()
                .map(this::toRequest)
                .toList();
    }

    default StatYearRequest toRequest(StatYear statYear) {
        if (statYear == null || statYear.getYearStat() == null) {
            return null;
        }
        return new StatYearRequest(
                statYear.getYearStat().getYearStat(),
                statYear.getDistributionRate(),
                statYear.getSharePrice(),
                statYear.getReconstitutionValue(),
                statYear.getScpi() != null ? statYear.getScpi().getId() : null
        );
    }

    default List<StatYearRequest> toRequestStatYearList(List<StatYear> statYears) {
        return statYears == null ? Collections.emptyList() :
                statYears.stream().map(this::toRequest).toList();
    }

    @Named("mapYearStat")
    default StatYearId mapYearStat(Integer yearStat) {
        if (yearStat == null) {
            return null;
        }
        return new StatYearId(yearStat);
    }

}

