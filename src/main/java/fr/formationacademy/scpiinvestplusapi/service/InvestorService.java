package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestorMapper investorMapper;
    private UserService userService;

    public InvestorService(InvestorRepository investorRepository, InvestorMapper investorMapper) {
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


    public List<InvestorDTO> getAllInvestors() {
        List<Investor> investors = investorRepository.findAll();
        return investors.stream()
                .map(investorMapper::toDTO)
                .toList();
    }


    public Optional<Investor> getInvestorByEmail(String email) {
        return investorRepository.findById(email);
    }

    public Investor getCurrentInvestor() {
        String email = userService.getEmail();

        return investorRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Aucun investisseur trouvé pour l'email: {}", email);
                    return new RuntimeException("Investor not found for email: " + email);
                });
    }
}