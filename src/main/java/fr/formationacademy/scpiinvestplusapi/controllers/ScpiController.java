package fr.formationacademy.scpiinvestplusapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import fr.formationacademy.scpiinvestplusapi.dto.DetailsDTO;

import fr.formationacademy.scpiinvestplusapi.services.ScpiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Scpi", description = "API for Scpi ")
@RequestMapping(path = "api/v1/application/Scpi")
public class ScpiController {
    @Autowired
    private final ScpiService scpiService;
    
    public ScpiController(ScpiService scpiService){
        this.scpiService = scpiService;
    }
    @Operation(
        summary = "Details of a specific SCPI",
        description = "Get all the details from a given SCPI with a specific id as parameter.",
        responses = {
            @ApiResponse(responseCode = "200", description = "An Details object containing different inforamtion." , useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = "Could not find the details of the selected scpi." , useReturnTypeSchema = true), 
        }    
    )
    @GetMapping("/details/{scpid}")
    public ResponseEntity<DetailsDTO> getDetailsById(@PathVariable int scpid) {
        return ResponseEntity.ok( this.scpiService.gettAllDetails( scpid));
    }
    
}
