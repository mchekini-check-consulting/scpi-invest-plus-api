package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.service.InvestmentService;
import fr.formationacademy.scpiinvestplusapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/investment")
@Tag(name = "Investissements", description = "API pour la gestion des investissements")
public class InvestmentResource {

    private final InvestmentService investmentService;
    private final InvestmentMapper investmentMapper;
    private final UserService userService;

    public InvestmentResource(InvestmentService investmentService, InvestmentMapper investmentMapper, UserService userService) {
        this.investmentService = investmentService;
        this.investmentMapper = investmentMapper;
        this.userService = userService;
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
    public ResponseEntity<InvestmentDto> createInvestment(@RequestBody InvestmentDto investmentDto) throws GlobalException {
        InvestmentDto investmentDtoResult = investmentService.saveInvestment(investmentDto);

        return new ResponseEntity<>(investmentDtoResult, HttpStatus.CREATED);

    }

    @Operation(summary = "Recupérer la liste des investissements d'un investisseur authentifié", description = "Cette API permet d'obtenir la liste complète des investissements d'un investisseur actuellement authentifié.")
    @GetMapping
    public ResponseEntity<List<InvestmentDtoOut>> getInvestments() {
        try{
            return ResponseEntity.ok(investmentMapper.toDtoOutList(investmentService.getInvestments(userService.getEmail())));
        } catch (Exception e){
            return ResponseEntity.status(403).body(null);
        }
    }
}
