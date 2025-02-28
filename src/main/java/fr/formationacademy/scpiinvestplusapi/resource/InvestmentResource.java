package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investment")
public class InvestmentResource {

    private final InvestmentService investmentService;

    public InvestmentResource(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @Operation(summary = "Créer un nouvel investissement", description = "Cette API permet de créer un nouvel investissement pour une SCPI et un investisseur donnée.")
    @PostMapping
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
