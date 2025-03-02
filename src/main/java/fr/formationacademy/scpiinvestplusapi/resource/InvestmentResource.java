package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investment")
@Tag(name = "Investissements", description = "API pour la gestion des investissements")
public class InvestmentResource {

    private final InvestmentService investmentService;

    public InvestmentResource(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    @Operation(
            summary = "Créer un nouvel investissement",
            description = "Cette API permet de créer un nouvel investissement pour une SCPI et un investisseur donné.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Investissement créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
            }
    )
    public InvestmentDto createInvestment(@RequestBody InvestmentDto investmentDto) {
        return investmentService.saveInvestment(investmentDto);
    }

    @Operation(summary = "Récupérer les investissements d'un investisseur", description = "Récupère tous les investissements liés à un investisseur spécifique à partir de son email.")
    @GetMapping("/investor/{email}")
    public List<Investment> getInvestmentsByInvestor(@PathVariable String email) {
        return investmentService.getInvestmentsByInvestorEmail(email);
    }

    @Operation(summary = "Récupérer les investissements d'une SCPI", description = "Renvoie la liste des investissements associés à une SCPI spécifique via son ID.")
    @GetMapping("/scpi/{id}")
    public List<Investment> getInvestmentsByScpi(@PathVariable Integer id) {
        return investmentService.getInvestmentsByScpiId(id);
    }
}
