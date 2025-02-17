package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.service.InvestorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "investors")
@Tag(name = "Investor", description = "API pour la gestion des investisseurs")
public class InvestorResource {

    private final InvestorService investorService;


    public InvestorResource(InvestorService investorService) {
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
            return ResponseEntity.ok(investorService.getAllInvestors());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Récupérer un investisseur par email")
    @GetMapping("/{email}")
    public ResponseEntity<Investor> getInvestorByEmail(@PathVariable String email) {
        return investorService.getInvestorByEmail(email).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(summary = "Mettre à jour un investisseur")
    @PatchMapping("/{email}")
    public ResponseEntity<Investor> updateInvestor(@PathVariable String email, @RequestBody InvestorDTO investorDTO) {
        Investor updatedInvestor = investorService.updateInvestor(email, investorDTO);
        return ResponseEntity.ok(updatedInvestor);
    }


}