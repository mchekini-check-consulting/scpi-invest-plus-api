package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.CriteriaIn;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDocumentDTO;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSearchCriteriaDto;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Tag(name = "Recherche des SCPI", description = "API pour faire des recherches avec Elasticsearch")
@RestController
@RequestMapping("/api/v1/scpi")
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
            @Parameter(description = "Taux de distribution minimum", example = "10")
            @RequestParam(required = false) BigDecimal distributionRate,
            @Parameter(description = "Montant minimum de souscription", example = "100")
            @RequestParam(required = false) Integer minimumSubscription,
            @Parameter(description = "Frais de souscription (true = avec frais, false = sans frais)")
            @RequestParam(required = false) Boolean subscriptionFees,
            @Parameter(description = "Fréquence de paiement (Mensuelle ou Annuel)", example = "Mensuelle")
            @RequestParam(required = false) String frequencyPayment,
            @Parameter(description = "Liste des locations pour la recherche", example = "[\"France\", \"Italie\"]")
            @RequestParam(required = false) List<String> locations,
            @Parameter(description = "Liste des secteurs pour la recherche", example = "[\"Santé\", \"Bureaux\"]")
            @RequestParam(required = false) List<String> sectors
    ) {
        try {

            ScpiSearchCriteriaDto criteria = new ScpiSearchCriteriaDto();
            criteria.setName(name);
            criteria.setDistributionRate(distributionRate);
            criteria.setMinimumSubscription(minimumSubscription);
            criteria.setSubscriptionFees(subscriptionFees);
            criteria.setFrequencyPayment(frequencyPayment);
            criteria.setLocations(locations);
            criteria.setSectors(sectors);

            List<ScpiDocumentDTO> results = scpiSearchService.searchScpi(criteria);

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

    @Operation(
            summary = "Récupérer les SCPI avec scheduledPayment à true",
            description = "Retourne toutes les SCPI de l'index Elasticsearch dont le champ 'scheduledPayment' est à true"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des SCPI avec scheduledPayment à true"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })

    @GetMapping("/scheduled-payment")
    public ResponseEntity<?> getScpisWithScheduledPayment() {
        try {
            List<ScpiDocumentDTO> results = scpiSearchService.getScpisWithScheduledPayment();
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Erreur lors de la récupération des SCPI avec scheduledPayment à true",
                            "details", e.getMessage()
                    ));
        }
    }


        @PostMapping("/ScoreSearch")
        public ResponseEntity<List<ScpiDocumentDTO>> searchScpi(@RequestBody List<CriteriaIn> criteria)
                        throws IOException {
                for (CriteriaIn criteriaIn : criteria) {
                        System.out.println("critere X : " + criteriaIn.getName());
                }
                List<ScpiDocumentDTO> result = scpiSearchService.searchScoredScpi(criteria);
                return ResponseEntity.ok(result);
        }

}
