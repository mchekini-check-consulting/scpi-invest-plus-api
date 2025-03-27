package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.entity.ScpiIndex;
import fr.formationacademy.scpiinvestplusapi.service.ScpiIndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recherche des SCPI", description = "API pour faire des recherches avec Elasticsearch")
@RestController // Remplacement de @Controller par @RestController pour éviter d'écrire @ResponseBody
@RequestMapping("/api/v1/scpiSearch")
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
    public Iterable<ScpiIndex> saveMultipleScpiSearch(@RequestBody List<ScpiIndex> scpiSearchList) {
        return scpiSearchService.saveMultipleScpiSearch(scpiSearchList);
    }
}
