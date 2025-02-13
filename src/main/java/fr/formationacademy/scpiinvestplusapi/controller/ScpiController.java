package fr.formationacademy.scpiinvestplusapi.controller;

import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
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

@RestController
@Tag(name = "Scpi", description = "API for Scpi ")
@RequestMapping(path = "api/v1/application/Scpi")
public class ScpiController {
    @Autowired
    private final ScpiServiceImpl scpiService;
    @Autowired
    private final StatYearServiceImpl statYearService;
    public ScpiController(ScpiServiceImpl scpiService, StatYearServiceImpl statYearService){
        this.scpiService = scpiService;
        this.statYearService = statYearService;

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
        System.out.println("Received scpid: " );
        return new ResponseEntity<>(scpiService.getScpiDetailsById(scpid), HttpStatus.OK);
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
