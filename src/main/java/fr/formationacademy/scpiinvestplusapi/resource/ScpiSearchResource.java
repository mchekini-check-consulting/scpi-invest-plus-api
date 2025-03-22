package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.entity.ScpiSearch;
import fr.formationacademy.scpiinvestplusapi.service.ScpiSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recherche des SCPI", description = "API pour faire des recherches avec Elasticsearch")
@RestController // Remplacement de @Controller par @RestController pour éviter d'écrire @ResponseBody
@RequestMapping("/api/v1/scpiSearch")
public class ScpiSearchResource {

    private final ScpiSearchService scpiSearchService;

    public ScpiSearchResource(ScpiSearchService scpiSearchService) {
        this.scpiSearchService = scpiSearchService;
    }

    @Operation(
            summary = "Sauvegarde des recherches SCPI",
            description = "Permet d'enregistrer plusieurs recherches SCPI dans Elasticsearch"
    )
    @PostMapping
    public Iterable<ScpiSearch> saveMultipleScpiSearch(@RequestBody List<ScpiSearch> scpiSearchList) {
        return scpiSearchService.saveMultipleScpiSearch(scpiSearchList);
    }
}
