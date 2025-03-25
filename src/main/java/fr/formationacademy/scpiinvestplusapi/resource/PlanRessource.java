package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.PlanDto;
import fr.formationacademy.scpiinvestplusapi.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@Tag(name = "Gestion des Plans", description = "Points d'accès pour la gestion des plans d'abonnement")
@RestController
@RequestMapping(APP_ROOT + "plans")
public class PlanRessource {

    private final PlanService planService;

    public PlanRessource(PlanService planService) {
        this.planService = planService;
    }

    @Operation(summary = "Obtenir tous les plans", description = "Récupère tous les plans d'abonnement disponibles.", tags = {"Gestion des Plans"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des plans récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanDto.class))}),
            @ApiResponse(responseCode = "401", description = "Non autorisé - Authentification requise",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content)
    })
    @GetMapping
    public List<PlanDto> getAllPlans() {
        return planService.getAllPlans();
    }

}

