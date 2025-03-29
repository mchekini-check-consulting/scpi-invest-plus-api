package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.KeycloakWebhookRequest;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.service.InvestorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "keycloak/webhook/")
@Slf4j
public class KeycloakWebhook {
    private final InvestorService investorService;

    public KeycloakWebhook(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PostMapping
    public ResponseEntity<String> createInvestorFromKeycloak(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody KeycloakWebhookRequest body
    ) throws GlobalException {
        log.debug("Received Keycloak Webhook Event: {}", body.getType());
        if (!"REGISTER".equalsIgnoreCase(body.getType())) {
            return ResponseEntity.noContent().build();
        }
        investorService.createInvestorFromKeycloak(authHeader, body);
        return ResponseEntity.ok("Investor registered successfully");
    }
}
