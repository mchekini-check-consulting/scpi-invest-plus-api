package fr.formationacademy.scpiinvestplusapi.controller;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDTO;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.dto.SectorDTO;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.service.LocationService;
import fr.formationacademy.scpiinvestplusapi.service.ScpiService;
import fr.formationacademy.scpiinvestplusapi.service.SectorService;
import fr.formationacademy.scpiinvestplusapi.service.StatYearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Scpi", description = "API for Scpi ")
@RequestMapping(path = "api/v1/Scpi")
public class ScpiController {
    private final ScpiService scpiService;
    private final StatYearService statYearService;
    private final LocationService locationService;
    private final SectorService sectorService;

    public ScpiController(ScpiService scpiService, StatYearService statYearService, LocationService locationService, SectorService sectorService) {
        this.scpiService = scpiService;
        this.statYearService = statYearService;
        this.locationService = locationService;
        this.sectorService = sectorService;

    }

    @Operation(
            summary = "Details of a specific SCPI",
            description = "Get all the details from a given SCPI with a specific id as parameter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A Details object containing different information.", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "500", description = "Could not find the details of the selected SCPI.", useReturnTypeSchema = true),
            }
    )
    @GetMapping("/details/{scpid}")
    public ResponseEntity<ScpiDTO> getDetailsById(@PathVariable Integer scpid) {
        ScpiDTO scpiDetails = scpiService.getScpiDetailsById(scpid);
        List<StatYear> listStat = this.statYearService.getStatYearsForScpi(scpid);
        List<Location> listLocations = this.locationService.getLocationById(scpid);
        List<Sector> listSectors = this.sectorService.getSectorsForScpi(scpid);
        List<StatYearDTO> statYearDTOs = listStat.stream()
                .map(statYear -> StatYearDTO.builder()
                        .year(statYear.getYearStat().getYearStat())
                        .distributionRate(statYear.getDistributionRate())
                        .sharePrice(statYear.getSharePrice())
                        .reconstitutionValue(statYear.getReconstitutionValue())
                        .scpiId(statYear.getScpi().getId())
                        .build())
                .collect(Collectors.toList());
        scpiDetails.setStatYears(statYearDTOs);
        List<LocationDTO> locationDTOs = listLocations.stream()
                .map(location -> LocationDTO.builder()
                        .country(location.getId().getCountry())
                        .countryPercentage(location.getCountryPercentage())
                        .build())
                .collect(Collectors.toList());
        scpiDetails.setLocations(locationDTOs);
        List<SectorDTO> sectorDTOs = listSectors.stream()
                .map(sector -> SectorDTO.builder()
                        .name(sector.getId().getName())
                        .sectorPercentage(sector.getSectorPercentage())
                        .build())
                .collect(Collectors.toList());
        scpiDetails.setSectors(sectorDTOs);
        return new ResponseEntity<>(scpiDetails, HttpStatus.OK);
    }


}
