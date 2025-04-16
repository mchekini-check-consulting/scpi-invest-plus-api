package fr.formationacademy.scpiinvestplusapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentStateDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentStatisticsDtoOut;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "investment")
@Tag(name = "Investissements", description = "API pour la gestion des investissements")
@Slf4j
public class InvestmentResource {

    private final InvestmentService investmentService;

    public InvestmentResource(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    @Operation(
            summary = "Créer un nouvel investissement",
            description = "Cette API permet de créer un nouvel investissement pour une SCPI donnée.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Investment successfully created"),
                    @ApiResponse(responseCode = "400", description = "Requête invalide"),
                    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
            }
    )
    public ResponseEntity<InvestmentDto> createInvestment(@RequestBody InvestmentDto investmentDto) throws GlobalException, JsonProcessingException {
        InvestmentDto investmentDtoResult = investmentService.saveInvestment(investmentDto);

        return new ResponseEntity<>(investmentDtoResult, HttpStatus.CREATED);

    }

    @Operation(summary = "Récupérer les investissements paginés selon l'état",
            description = "Cette API permet de récupérer une liste paginée des investissements, filtrée par état et retourner aussi le nombre total d'investissements")
    @GetMapping
    public ResponseEntity<InvestmentStateDtoOut> getInvestments(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false, value = "state", defaultValue = "VALIDATED") String state
    ) throws GlobalException {
        InvestmentStateDtoOut investments = investmentService.getPortfolio(pageable, state);
        return ResponseEntity.ok(investments);
    }

    @Operation(summary = "Récupérer les statistiques du portefeuille d'investissements",
            description = "Cette API retourne uniquement les statistiques calculées des investissements selon leur état.")
    @GetMapping("/stats")
    public ResponseEntity<InvestmentStatisticsDtoOut> getInvestmentStats() throws GlobalException {

        InvestmentStatisticsDtoOut stats = investmentService.getInvestmentStats();
        return ResponseEntity.ok(stats);
    }


}
