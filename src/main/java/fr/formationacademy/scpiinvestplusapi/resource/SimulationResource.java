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

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT +"simulation")
@Tag(name = "Simulation", description = "API pour la gestion des simulations")
public class SimulationResource {
    private final SimulationService simulationService;

    @Autowired
    public SimulationResource(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Operation(summary = "Obtenir toutes les simulations.",
            description = "Obtenir une liste des simulation stockées en base de données pour l'utilisateur identifé.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La liste des simulations ont été récupérées",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimulationDToOut.class))),
            @ApiResponse(responseCode = "204", description = "Pas de simulations trouvées.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimulationDToOut.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<SimulationDToOut>> getAllSimulations() {
        List<SimulationDToOut> simulations = this.simulationService.getSimulations();
        if (simulations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(simulations, HttpStatus.OK);
    }

    @Operation(summary = "Création d'une nuovelle simulation",
            description = "Création d'une nouvelle simulation à partir des données reçus par l'application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Simulation crée",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimulationDToOut.class))),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la caréation")
    })
    @PostMapping
    public ResponseEntity<SimulationDToOut> createSimulation(@RequestBody SimulationInDTO simulationInDTO) throws GlobalException {
        SimulationDToOut simulationDToOut = simulationService.addSimulation(simulationInDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(simulationDToOut);
    }

    @Operation(summary = "Supprimer une simulation",
            description = "Supprimer une simulation à l'aide de son identifiant pour l'utilisateur connecté.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppression de la simulation réussie.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Simulation.class))),
            @ApiResponse(responseCode = "404", description = "La simulation n'existe pas dans la base de données")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SimulationDToOut> removeSimulation(@PathVariable Integer id) throws GlobalException {
        SimulationDToOut simulationDToOut = simulationService.deleteSimulation(id);
        return new ResponseEntity<>(simulationDToOut, HttpStatus.OK);
    }

    @Operation(summary = "Charger une simulation en utilisant identifiant de la simulation",
            description = "Selectionner une simulation appartenant à  l'utilisateur en fonction de l'identifiant introduit.")
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
