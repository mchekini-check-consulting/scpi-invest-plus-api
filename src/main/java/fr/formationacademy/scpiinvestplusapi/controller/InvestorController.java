package fr.formationacademy.scpiinvestplusapi.controller;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.service.InvestorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/investors")
@Tag(name = "Investor", description = "API pour la gestion des investisseurs")
public class InvestorController {

    private final InvestorService investorService;


    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @Operation(summary = "Créer un investisseur")
    @PostMapping
    public ResponseEntity<Investor> createInvestor(@RequestBody InvestorDTO investorDTO) {
        Investor createdInvestor = investorService.createInvestor(investorDTO);
        return ResponseEntity.ok(createdInvestor);
    }
    @Operation(summary = "Récupérer tous les investiasseurs")
    @GetMapping
    public ResponseEntity<List<InvestorDTO>> getAllInvestors() {
        try {
            List<InvestorDTO> investors = investorService.getAllInvestors();
            return ResponseEntity.ok(investors);
        } catch (Exception e) {
            // En cas d'exception, retourner une erreur 500
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Récupérer un investisseur par email")
    @GetMapping("/{email}")
    public ResponseEntity<Investor> getInvestorByEmail(@PathVariable String email) {
        Optional<Investor> investor = investorService.getInvestorByEmail(email);
        return investor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(summary = "Mettre à jour un investisseur")
    @PatchMapping("/{email}")
    public ResponseEntity<Investor> updateInvestor(@PathVariable String email, @RequestBody InvestorDTO investorDTO) {
        Investor updatedInvestor = investorService.updateInvestor(email, investorDTO);
        return ResponseEntity.ok(updatedInvestor);
    }


}
