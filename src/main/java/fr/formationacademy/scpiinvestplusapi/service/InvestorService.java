package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestorMapper investorMapper;
    private final UserService userService;

    public InvestorService(InvestorRepository investorRepository, InvestorMapper investorMapper, UserService userService) {
        this.investorRepository = investorRepository;
        this.investorMapper = investorMapper;
        this.userService = userService;
    }

    public Investor createOrUpdateInvestor(String email, InvestorDTO investorDTO) {
        log.info("Creating Investor with email: " + email);
        log.info("Investor email: " + investorDTO);
        return investorRepository.findById(email)
                .map(existingInvestor -> {
                    Investor updatedInvestor = investorMapper.toEntity(investorDTO);
                    updatedInvestor.setEmail(email);
                    return investorRepository.save(updatedInvestor);
                })
                .orElseGet(() -> {
                    Investor newInvestor = investorMapper.toEntity(investorDTO);
                    newInvestor.setEmail(email);
                    return investorRepository.save(newInvestor);
                });
    }


    public Investor createInvestor(InvestorDTO investorDTO) throws GlobalException {
        log.info("Creating new Investor");

        Investor newInvestor = investorMapper.toEntity(investorDTO);

        if (investorRepository.existsById(newInvestor.getEmail())) {
            throw new GlobalException(HttpStatus.CONFLICT, "An investor with this email already exists.");
        }

        return investorRepository.save(newInvestor);
    }

    public Investor updateInvestor(String email, InvestorDTO investorDTO) throws GlobalException{
        log.info("Updating Investor with email: " + email);
        return investorRepository.findById(email)
                .map(existingInvestor -> {
                    Investor updatedInvestor = investorMapper.toEntity(investorDTO);
                    updatedInvestor.setEmail(email);
                    return investorRepository.save(updatedInvestor);
                })
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,"Investor not found with email: " + email));
    }



    public Optional<Investor> getInvestorByEmail(String email) {
        return investorRepository.findById(email);
    }

    public Investor getCurrentInvestor() {
        String email = userService.getEmail();
        log.info("Tentative de récupération de l'investisseur avec l'email: {}", email);

        return investorRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Aucun investisseur trouvé pour l'email: {}", email);
                    return new RuntimeException("Investor not found for email: " + email);
                });
    }
}