package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.service.SimulationService;
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
@Tag(name = "Simulation", description = "API pour la gestion des simulations")
public class SimulationResource {
    private final SimulationService simulationService;

    @Autowired
    public SimulationResource(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Operation(summary = "Get all simulations",
            description = "Retrieves a list of all simulations. Returns 204 if no simulations are found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of simulations retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimulationDToOut.class))),
            @ApiResponse(responseCode = "204", description = "No simulations found")
    })
    @GetMapping
    public ResponseEntity<List<SimulationDToOut>> getAllSimulations() {
        List<SimulationDToOut> simulations = this.simulationService.getSimulations();
        if (simulations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(simulations, HttpStatus.OK);
    }

    @Operation(summary = "Create a new simulation",
            description = "Creates a simulation for an existing investor identified by email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Simulation created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Simulation.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<SimulationDToOut> createSimulation(@RequestBody SimulationInDTO simulationInDTO) {
        SimulationDToOut simulationDToOut = simulationService.addSimulation(simulationInDTO);
        if (simulationDToOut == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(simulationDToOut);
    }
    @Operation(summary = "Delete a simulation",
            description = "Delete a simulation using an ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulation deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Simulation.class))),
            @ApiResponse(responseCode = "404", description = "Simulation ID not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SimulationDToOut> removeSimulation(@PathVariable Integer id) {
        SimulationDToOut simulationDToOut = simulationService.deleteSimulation(id);
        if (simulationDToOut == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(simulationDToOut, HttpStatus.OK);
    }

    @Operation(summary = "Get simulation by ID",
            description = "Retrieve a simulation by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulation retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimulationDToOut.class))),
            @ApiResponse(responseCode = "404", description = "Simulation ID not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SimulationDToOut> getSimulationById(@PathVariable Integer id) throws GlobalException {
        SimulationDToOut simulationDToOut = simulationService.getSimulationById(id);
        return new ResponseEntity<>(simulationDToOut, HttpStatus.OK);
    }
}
