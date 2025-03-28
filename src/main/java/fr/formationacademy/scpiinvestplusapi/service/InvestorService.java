package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.dto.KeycloakWebhookRequest;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestorMapper investorMapper;
    
    @Value("${spring.webhook.username}")
    private String webhookUsername;
    @Value("${spring.webhook.password}")
    private String webhookPassword;


    public InvestorService(InvestorRepository investorRepository, InvestorMapper investorMapper) {
        this.investorRepository = investorRepository;
        this.investorMapper = investorMapper;
    }


    public Investor createInvestor(InvestorDTO investorDTO) throws GlobalException {
        log.info("Creating new Investor");

        Investor newInvestor = investorMapper.toEntity(investorDTO);

        if (investorRepository.existsById(newInvestor.getEmail())) {
            throw new GlobalException(HttpStatus.CONFLICT, "An investor with this email already exists.");
        }

        log.info("saving ne Investor");
        return investorRepository.save(newInvestor);

    }

    public Investor updateInvestor(String email, InvestorDTO investorDTO) throws GlobalException {
        log.info("Updating Investor with email: " + email);
        return investorRepository.findById(email)
                .map(existingInvestor -> {
                    Investor updatedInvestor = investorMapper.toEntity(investorDTO);
                    updatedInvestor.setEmail(email);
                    return investorRepository.save(updatedInvestor);
                })
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Investor not found with email: " + email));
    }


    public Optional<Investor> getInvestorByEmail(String email) {
        return investorRepository.findById(email);
    }


    public void createInvestorFromKeycloak(String authHeader, KeycloakWebhookRequest body) throws GlobalException {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String base64Credentials = authHeader.substring("Basic ".length());
        String decodedCredentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] parts = decodedCredentials.split(":", 2);

        if (parts.length != 2 || !isValidUser(parts[0], parts[1])) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Invalid Webhook Credentials");
        }
        log.info("Authentication Successful");

        Investor investor = Investor.builder()
                .email(body.details.email)
                .firstName(body.details.first_name)
                .lastName(body.details.last_name)
                .maritalStatus("single")
                .numberOfChildren("0")
                .annualIncome(0)
                .build();

        log.info("Saving investor: {}", investor);
        investorRepository.save(investor);
    }

    private boolean isValidUser(String username, String password) {
        return webhookUsername.equals(username) && webhookPassword.equals(password);
    }

}