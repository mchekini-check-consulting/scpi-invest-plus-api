package fr.formationacademy.scpiinvestplusapi.service.impl;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import fr.formationacademy.scpiinvestplusapi.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;  // Ajout de l'import pour ResponseEntity
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvestorServiceImpl implements InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestorMapper investorMapper;

    @Autowired
    public InvestorServiceImpl(InvestorRepository investorRepository, InvestorMapper investorMapper) {
        this.investorRepository = investorRepository;
        this.investorMapper = investorMapper;
    }

    @Override
    public Investor createInvestor(InvestorDTO investorDTO) {
        Investor investor = investorMapper.toEntity(investorDTO);
        return investorRepository.save(investor);
    }

    @Override
    public Investor updateInvestor(String email, InvestorDTO investorDTO) {
        return investorRepository.findById(email)
                .map(existingInvestor -> {
                    Investor updatedInvestor = investorMapper.toEntity(investorDTO);
                    updatedInvestor.setEmail(email); // Conserver l'email
                    return investorRepository.save(updatedInvestor);
                })
                .orElseThrow(() -> new RuntimeException("Investor not found with email: " + email));
    }

    @Override
    public List<InvestorDTO> getAllInvestors() {
        List<Investor> investors = investorRepository.findAll();
        return investors.stream()
                .map(investorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Investor> getInvestorByEmail(String email) {
        return investorRepository.findById(email);
    }
}
