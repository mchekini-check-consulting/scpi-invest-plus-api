package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.entity.ScpiIndex;
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
            summary = "Sauvegarde des recherches SCPI",
            description = "Permet d'enregistrer plusieurs recherches SCPI dans Elasticsearch"
    )
    @PostMapping
    public boolean saveMultipleScpiSearch(@RequestBody List<ScpiIndex> scpiSearchList) throws IOException {
        return scpiSearchService.saveMultipleScpiIndex(scpiSearchList);
    }

    @Operation(
            summary = "Rechercher une scpi par son nom",
            description = "Retourne une scpi avec le nom adéquat"
    )
    @GetMapping("/SearchScpi")
    public ScpiIndex SearchScpi(@RequestParam String name) throws IOException {
        return scpiSearchService.getScpiByName(name);
    }

    @Operation(
            summary = "Rechercher une scpi par son nom avec une tolérance de 2 erreurs",
            description = "Retourne une scpi avec le nom adéquat"
    )
    @GetMapping("/FuzzySearch")
    public List<ScpiIndex> FuzzySearch(@RequestParam String name) throws IOException {
        return scpiSearchService.getFuzzyScpiByName(name);
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
            @Parameter(description = "Nom de la SCPI (recherche approximative)", example = "Paris")
            @RequestParam(required = false) String name,

            @Parameter(description = "Montant minimum de souscription", example = "1000")
            @RequestParam(required = false) Integer minimumSubscription) {

        try {
            List<ScpiIndex> results = scpiSearchService.searchScpi(name, minimumSubscription);

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
