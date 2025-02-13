package fr.formationacademy.scpiinvestplusapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;

import fr.formationacademy.scpiinvestplusapi.service.impl.ScpiServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Scpi", description = "API for Scpi ")
@RequestMapping(path = "api/v1/application/Scpi")
public class ScpiController {
    @Autowired
    private final ScpiServiceImpl scpiService;
    
    public ScpiController(ScpiServiceImpl scpiService){
        this.scpiService = scpiService;
    }
    /*   @Operation(
          summary = "Details of a specific SCPI",
          description = "Get all the details from a given SCPI with a specific id as parameter.",
          responses = {
              @ApiResponse(responseCode = "200", description = "An Details object containing different inforamtion." , useReturnTypeSchema = true),
              @ApiResponse(responseCode = "500", description = "Could not find the details of the selected scpi." , useReturnTypeSchema = true),
          }
      )
      @GetMapping("/details/{scpid}")
      public ResponseEntity<ScpiDTO> getDetailsById(@PathVariable int scpid) {
          return ResponseEntity.ok( this.scpiService.gettAllDetails( scpid));
      }
  */
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
        ScpiDTO createdScpi = scpiService.addScpi(scpiDTO);
        return new ResponseEntity<>(createdScpi, HttpStatus.CREATED);
    }
    
}
