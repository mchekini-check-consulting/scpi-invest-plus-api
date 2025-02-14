package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.service.ScpiService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
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
}
