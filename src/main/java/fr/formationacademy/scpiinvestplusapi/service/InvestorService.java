package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestorMapper investorMapper;

    public InvestorService(InvestorRepository investorRepository, InvestorMapper investorMapper) {
        this.investorRepository = investorRepository;
        this.investorMapper = investorMapper;
    }


    public Investor createInvestor(InvestorDTO investorDTO) {
        Investor investor = investorMapper.toEntity(investorDTO);
        return investorRepository.save(investor);
    }


    public Investor updateInvestor(String email, InvestorDTO investorDTO) {
        return investorRepository.findById(email)
                .map(existingInvestor -> {
                    Investor updatedInvestor = investorMapper.toEntity(investorDTO);
                    updatedInvestor.setEmail(email); // Conserver l'email
                    return investorRepository.save(updatedInvestor);
                })
                .orElseThrow(() -> new RuntimeException("Investor not found with email: " + email));
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
}
