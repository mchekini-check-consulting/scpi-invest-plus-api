package fr.formationacademy.scpiinvestplusapi.controller;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDTO;
import fr.formationacademy.scpiinvestplusapi.dto.SectorDTO;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.service.impl.LocationServiceImpl;
import fr.formationacademy.scpiinvestplusapi.service.impl.SectorServiceImpl;
import fr.formationacademy.scpiinvestplusapi.service.impl.StatYearServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;

import fr.formationacademy.scpiinvestplusapi.service.impl.ScpiServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Scpi", description = "API for Scpi ")
@RequestMapping(path = "api/v1/application/Scpi")
public class ScpiController {
    @Autowired
    private final ScpiServiceImpl scpiService;
    @Autowired
    private final StatYearServiceImpl statYearService;
    @Autowired
    private final LocationServiceImpl locationService;
    @Autowired
    private final SectorServiceImpl sectorService;

    public ScpiController(ScpiServiceImpl scpiService, StatYearServiceImpl statYearService, LocationServiceImpl locationService, SectorServiceImpl sectorService){
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
        // Récupération des détails SCPI
        ScpiDTO scpiDetails = scpiService.getScpiDetailsById(scpid);

        // Récupération des StatYear pour la SCPI donnée
        List<StatYear> listStat = this.statYearService.getStatYearsForScpi(scpid);
        // Récupération des Locations
        List<Location> listLocations = this.locationService.getLocationById(scpid);

        // Répération des Sectors
        List<Sector> listSectors = this.sectorService.getSectorsForScpi(scpid);
        // Conversion des StatYear en StatYearDTO
        List<StatYearDTO> statYearDTOs = listStat.stream()
                .map(statYear -> StatYearDTO.builder()
                        .year(statYear.getYearStat().getYearStat())
                        .distributionRate(statYear.getDistributionRate())
                        .sharePrice(statYear.getSharePrice())
                        .reconstitutionValue(statYear.getReconstitutionValue())
                        .scpiId(statYear.getScpi().getId())  // Assurez-vous que scpiId est correctement attribué
                        .build())
                .collect(Collectors.toList());

        // Ajout des StatYearDTO au ScpiDTO
        scpiDetails.setStatYears(statYearDTOs);


        List<LocationDTO> locationDTOs = listLocations.stream()
                .map(location -> LocationDTO.builder()
                        .country(location.getId().getCountry())
                        .countryPercentage(location.getCountryPercentage())
                        .build())
                .collect(Collectors.toList());

        // Ajout des LocationDTO au ScpiDTO
        scpiDetails.setLocations(locationDTOs);

        List<SectorDTO> sectorDTOs = listSectors.stream()
                .map(sector -> SectorDTO.builder()
                        .name(sector.getId().getName())
                        .sectorPercentage(sector.getSectorPercentage())
                        .build())
                .collect(Collectors.toList());

// Ajout des SectorDTO au ScpiDTO
        scpiDetails.setSectors(sectorDTOs);

        // Retourner l'objet ScpiDTO avec les StatYearDTO
        return new ResponseEntity<>(scpiDetails, HttpStatus.OK);
    }



    @Operation(
            summary = "Create a new SCPI",
            description = "Add a new SCPI to the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "SCPI created successfully", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Invalid SCPI data", useReturnTypeSchema = true),
            }
    )

    @PostMapping("/add")
    public ResponseEntity<ScpiDTO> addScpi(@RequestBody ScpiDTO scpiDTO) {
        System.out.println("Received SCPI DTO: " + scpiDTO);

        ScpiDTO createdScpi = scpiService.addScpi(scpiDTO);
        System.out.println("Created SCPI DTO: " + scpiService.getScpiById(createdScpi.getId()));
        return new ResponseEntity<>(createdScpi, HttpStatus.CREATED);
    }
    
}
