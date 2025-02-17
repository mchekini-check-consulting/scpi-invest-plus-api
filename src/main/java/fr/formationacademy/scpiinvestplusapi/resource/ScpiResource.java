package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.service.ScpiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/scpi")
public class ScpiResource {
    private final ScpiService scpiService;

    public ScpiResource(ScpiService scpiService) {
        this.scpiService = scpiService;
    }
    
    @Operation(summary = "Récupérer la liste des SCPI", description = "Renvoie toutes les SCPI disponibles.")
    @GetMapping
    public ResponseEntity<List<ScpiDtoOut>> getScpi() {
        return ResponseEntity.ok(scpiService.getScpis());
    }
    @Operation(summary = "Retrieve SCPI details by ID",
            description = "Returns SCPI details as a DTO based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success - SCPI details retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScpiDtoOut.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "SCPI not found")
    })
    @GetMapping("/details/{scpid}")
    public ResponseEntity<ScpiDtoOut> getDetailsById(@PathVariable Integer scpid) {
        return ResponseEntity.ok(scpiService.getScpiDetailsById(scpid));
    }
}
