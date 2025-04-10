package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.service.InvestorService;
import fr.formationacademy.scpiinvestplusapi.service.KeycloakAdminService;
import fr.formationacademy.scpiinvestplusapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "investors")
@Tag(name = "Investor", description = "API pour la gestion des investisseurs")
public class InvestorResource {

    private final InvestorService investorService;
    private final UserService userService;
    private final KeycloakAdminService keycloakService;

    public InvestorResource(InvestorService investorService, UserService userService, KeycloakAdminService keycloakService) {
        this.investorService = investorService;
        this.userService = userService;
        this.keycloakService = keycloakService;
    }

    @Operation(
            summary = "Créer un investisseur",
            description = "Ajoute un nouvel investisseur en base de données et retourne ses informations."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Investisseur créé avec succès", content = @Content(schema = @Schema(implementation = Investor.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping("")
    public ResponseEntity<Investor> createInvestor(
            @RequestBody InvestorDTO investorDTO
    ) throws GlobalException {
        Investor investor = investorService.createInvestor(investorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(investor);
    }

    @Operation(
            summary = "Mettre à jour un investisseur",
            description = "Met à jour les informations d'un investisseur existant à partir de son email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Investisseur mis à jour avec succès", content = @Content(schema = @Schema(implementation = Investor.class))),
            @ApiResponse(responseCode = "404", description = "Investisseur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de mise à jour invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PatchMapping("")
    public ResponseEntity<Investor> updateInvestor(
            @RequestBody InvestorDTO investorDTO
    ) throws GlobalException {
        Investor updatedInvestor = investorService.updateInvestor(userService.getEmail(), investorDTO);
        return ResponseEntity.ok(updatedInvestor);
    }

    @Operation(summary = "Récupérer un investisseur par email")
    @GetMapping("")
    public ResponseEntity<Investor> getInvestorByEmail() {
        return investorService.getInvestorByEmail(userService.getEmail()).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public void updateRole(@RequestParam("currentRole") String currentRole,@RequestParam("newRole") String newRole) {
        keycloakService.updateRoleForUser(currentRole, newRole);
    }
}