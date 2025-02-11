package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.SectorDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ScpiMapper {

    @Mapping(target = "statYear", source = "statYears", qualifiedByName = "firstStatYear")
    @Mapping(target = "location", source = "locations", qualifiedByName = "highestPercentageLocation")
    @Mapping(target = "sector", source = "sectors", qualifiedByName = "highestPercentageSector")
    ScpiDtoOut scpiToScpiDtoOut(Scpi scpi);

    List<ScpiDtoOut> scpiToScpiDtoOut(List<Scpi> scpis);

    @Named("firstStatYear")
    default StatYearDtoOut getFirstStatYear(List<StatYear> statYears) {
        return (statYears != null && !statYears.isEmpty())
                ? StatYearDtoOut
                .builder()
                .distributionRate(statYears.get(0).getDistributionRate())
                .build()
                : null;
    }

    @Named("highestPercentageLocation")
    default LocationDtoOut getHighestPercentageLocation(List<Location> locations) {
        return (locations != null && !locations.isEmpty())
                ? locations.stream()
                .max(Comparator.comparing(Location::getCountryPercentage))
                .map(item -> LocationDtoOut
                        .builder()
                        .id(item.getId())
                        .countryPercentage(item.getCountryPercentage())
                        .build())
                .orElse(null)
                : null;
    }

    @Named("highestPercentageSector")
    default SectorDtoOut getHighestPercentageSector(List<Sector> sectors) {
        return (sectors != null && !sectors.isEmpty())
                ? sectors.stream()
                .max(Comparator.comparing(Sector::getSectorPercentage))
                .map(sector -> SectorDtoOut
                        .builder()
                        .id(sector.getId())
                        .sectorPercentage(sector.getSectorPercentage())
                        .build())
                .orElse(null)
                : null;
    }
}
