package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.dto.KeycloakUserDto;
import fr.formationacademy.scpiinvestplusapi.dto.KeycloakWebhookDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestorMapper investorMapper;
    private final KeycloakAdminService keycloakAdminService;

    public InvestorService(InvestorRepository investorRepository, InvestorMapper investorMapper, KeycloakAdminService keycloakAdminService) {
        this.investorRepository = investorRepository;
        this.investorMapper = investorMapper;
        this.keycloakAdminService = keycloakAdminService;
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


    public void createInvestorFromKeycloak(KeycloakWebhookDto body) throws GlobalException {
        KeycloakUserDto keycloakUser = keycloakAdminService.getUserFromKeycloak(body.getUserId());

        if (investorRepository.findByEmail(keycloakUser.getEmail()).isPresent()) {
            throw new GlobalException(HttpStatus.CONFLICT, "Investor with this email already exists.");
        }

        log.info("Creating Investor from Keycloak {}", keycloakUser);
        investorRepository.save(investorMapper.KeycloakUserToInvestor(keycloakUser));
    }
}