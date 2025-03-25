package fr.formationacademy.scpiinvestplusapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "investment")
@Tag(name = "Investissements", description = "API pour la gestion des investissements")
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

    @Operation(summary = "Recupérer la liste des investissements d'un investisseur authentifié", description = "Cette API permet d'obtenir la liste complète des investissements d'un investisseur actuellement authentifié.")
    @GetMapping
    public ResponseEntity<List<InvestmentDtoOut>> getInvestments() {
        return ResponseEntity.ok(investmentService.getInvestments());
    }
}
