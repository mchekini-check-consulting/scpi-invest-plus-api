package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.*;

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

}
