package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.dto.KeycloakWebhookDto;
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
public class webhookResource {
    private final InvestorService investorService;

    public webhookResource(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PostMapping("/keycloak/")
    public ResponseEntity<String> createInvestorFromKeycloak(@RequestBody KeycloakWebhookDto body) throws GlobalException {
        if (!"REGISTER".equalsIgnoreCase(body.getType())) {
            log.debug("Ignoring event of type: {}", body.getType());
            return ResponseEntity.noContent().build();
        }

        log.info("Received Keycloak Webhook Register Event: {}", body);
        
        investorService.createInvestorFromKeycloak(body);
        return ResponseEntity.ok("Investor registered successfully");
    }
}
