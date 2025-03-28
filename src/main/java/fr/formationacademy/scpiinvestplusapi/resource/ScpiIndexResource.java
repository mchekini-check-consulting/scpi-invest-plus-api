package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.entity.ScpiIndex;
import fr.formationacademy.scpiinvestplusapi.service.ScpiIndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Recherche des SCPI", description = "API pour faire des recherches avec Elasticsearch")
@RestController
@RequestMapping("/api/v1/scpi")
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
}
