package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.*;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.service.LocationService;
import fr.formationacademy.scpiinvestplusapi.service.ScpiService;
import fr.formationacademy.scpiinvestplusapi.service.SectorService;
import fr.formationacademy.scpiinvestplusapi.service.StatYearService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/scpi")
public class ScpiResource {
    private final ScpiService scpiService;
    private final StatYearService statYearService;
    private final LocationService locationService;
    private final SectorService sectorService;

    public ScpiResource(ScpiService scpiService, StatYearService statYearService, LocationService locationService, SectorService sectorService) {
        this.scpiService = scpiService;
        this.statYearService = statYearService;
        this.locationService = locationService;
        this.sectorService = sectorService;

    }
    @Operation(summary = "Récupérer la liste des SCPI", description = "Renvoie toutes les SCPI disponibles.")
    @GetMapping
    public ResponseEntity<List<ScpiDtoOut>> getScpi() {
        return ResponseEntity.ok(scpiService.getScpis());
    }
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
