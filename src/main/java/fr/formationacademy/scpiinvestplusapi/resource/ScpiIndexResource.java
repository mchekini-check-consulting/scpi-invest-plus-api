package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiIndexDto;
import fr.formationacademy.scpiinvestplusapi.service.ScpiIndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "Recherche des SCPI", description = "API pour faire des recherches avec Elasticsearch")
@RestController
@RequestMapping("/api/v1/scpiIndex")
public class ScpiIndexResource {

    private final ScpiIndexService scpiSearchService;

    public ScpiIndexResource(ScpiIndexService scpiSearchService) {
        this.scpiSearchService = scpiSearchService;
    }


    @Operation(
            summary = "Recherche de SCPI",
            description = "Recherche des SCPI par nom (approximatif) et/ou montant minimum de souscription (exact)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des SCPI correspondantes"),
            @ApiResponse(responseCode = "404", description = "Aucune SCPI trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchScpi(
            @Parameter(description = "Nom de la SCPI (recherche approximative)", example = "Comète")
            @RequestParam(required = false) String name,

            @Parameter(description = "", example = "10")
            @RequestParam(required = false) Float distributionRate,

            @Parameter(description = "", example = "100")
            @RequestParam(required = false) Float minimumSubscription,


            @RequestParam(required = false) Boolean subscriptionFees,

            @Parameter(description = "Fréquence de paiement (Mensuelle ou Annuel)", example = "Mensuelle")
            @RequestParam(required = false) String frequencyPayment,

            @Parameter(description = "Liste des locations pour la recherche", example = "[\"France\", \"Italie\"]")
            @RequestParam(required = false) List<String> locations,

            @Parameter(description = "Liste des secteurs pour la recherche", example = "[\"Santé\", \"Bureaux\"]")
            @RequestParam(required = false) List<String> sectors
    ) {

        try {
            List<ScpiIndexDto> results = scpiSearchService.searchScpi(name, distributionRate,minimumSubscription,subscriptionFees,frequencyPayment,locations,sectors);

            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "error",
                                "message", "Aucune SCPI ne correspond aux critères de recherche"
                        ));
            }

            return ResponseEntity.ok(results);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Erreur lors de la recherche",
                            "details", e.getMessage()
                    ));
        }
    }
}
