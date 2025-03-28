package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.KeycloakWebhookRequest;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.service.InvestorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "webhook")
@Slf4j
public class webhook {
    private final InvestorService investorService;

    public webhook(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PostMapping("/keycloak/")
    public ResponseEntity<String> createInvestorFromKeycloak(@RequestBody KeycloakWebhookRequest body) throws GlobalException {
        log.debug("Received Keycloak Webhook Event: {}", body.getType());

        if (!"REGISTER".equalsIgnoreCase(body.getType())) {
            log.debug("Ignoring event of type: {}", body.getType());
            return ResponseEntity.noContent().build();
        }

        investorService.createInvestorFromKeycloak(body);
        log.info("Investor registered successfully for user: {}", body.getDetails().getEmail());
        return ResponseEntity.ok("Investor registered successfully");
    }
}
