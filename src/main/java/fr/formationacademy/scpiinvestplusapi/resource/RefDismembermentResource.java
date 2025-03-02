package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.service.RefDismembermentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Référentiel Démembrement", description = "API pour gérer les données de démembrement en fonction du type de propriété")
@RestController
@RequestMapping("/api/ref-dismemberment")
public class RefDismembermentResource {

    private final RefDismembermentService service;

    public RefDismembermentResource(RefDismembermentService service) {
        this.service = service;
    }

    @Operation(summary = "Récupérer les données de démembrement par type de propriété", description = "Renvoie la liste des données de démembrement correspondant au type de propriété fourni.")
    @GetMapping("/{typeProperty}")
    public ResponseEntity<List<RefDismembermentDto>> getByTypeProperty(@PathVariable String typeProperty) {
        try {
            List<RefDismembermentDto> results = service.getByPropertyType(typeProperty);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}
