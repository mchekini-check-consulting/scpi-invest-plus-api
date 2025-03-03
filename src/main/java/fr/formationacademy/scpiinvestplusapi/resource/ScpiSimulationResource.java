package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import fr.formationacademy.scpiinvestplusapi.service.ScpiSimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/simulation")
@Tag(name = "Scpi Simulation", description = "API pour la gestion des scpi d'une simulation")
public class ScpiSimulationResource {
    private ScpiSimulationService scpiSimulationService;

    @Autowired
    public ScpiSimulationResource(ScpiSimulationService scpiSimulationService) {
        this.scpiSimulationService = scpiSimulationService;
    }

    @Operation(summary = "Add an SCPI simulation",
            description = "Creates a new SCPI simulation and returns the created object.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SCPI simulation created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScpiSimulation.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addScpi")
    public ResponseEntity<ScpiSimulationDToOut> addScpi(@RequestBody ScpiSimulationInDTO scpiSimulationIn) {
        ScpiSimulationDToOut scpiSimulationDToOut = this.scpiSimulationService.addScpiToSimulation(scpiSimulationIn);
        if (scpiSimulationDToOut != null) {
            return new ResponseEntity<>(scpiSimulationDToOut, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @Operation(summary = "Get List of SCPI simulation",
            description = "Get the lisf of SCPI simulation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SCPI simulation created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScpiSimulation.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/listScpiSimulation")
    public ResponseEntity<List<ScpiSimulationDToOut>> showScpiSimulation() {
        return new ResponseEntity<>(this.scpiSimulationService.getAllScpitSimulations(), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete SCPI from Simulation",
            description = "Deletes an SCPI entry from a simulation based on the provided simulationId and scpiId."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SCPI entry successfully deleted",
                    content = @Content(schema = @Schema(implementation = ScpiSimulationDToOut.class))),
            @ApiResponse(responseCode = "404", description = "SCPI entry or Simulation not found")
    })
    @DeleteMapping("/{simulationId}/{scpiId}")
    public ResponseEntity<ScpiSimulationDToOut> deleteScipiFromSimulation(@PathVariable Integer simulationId, @PathVariable Integer scpiId) {
        ScpiSimulationDToOut scpiSimulationDToOut = scpiSimulationService.deleteScipiFromSimulation(simulationId, scpiId);
        if (scpiSimulationDToOut != null) {
            return new ResponseEntity<>(scpiSimulationDToOut, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
