package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.dto.KeycloakWebhookRequest;
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


    public void createInvestorFromKeycloak(KeycloakWebhookRequest body) throws GlobalException {

        if (body.getDetails() == null) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Webhook payload missing 'details' section");
        }

        Investor investor = Investor.builder()
                .email(body.getDetails().getEmail())
                .firstName(body.getDetails().getFirstName())
                .lastName(body.getDetails().getLastName())
                .maritalStatus("single")
                .numberOfChildren("0")
                .annualIncome(0)
                .build();

        log.info("Saving investor: {}", investor);
        investorRepository.save(investor);
    }

}