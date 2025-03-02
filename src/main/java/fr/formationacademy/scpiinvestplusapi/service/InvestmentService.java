package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final ScpiRepository scpiRepository;
    private final InvestmentMapper investmentMapper;
    private final InvestorService investorService;

    public InvestmentService(InvestmentRepository investmentRepository, ScpiRepository scpiRepository,
            InvestmentMapper investmentMapper, InvestorService investorservice) {
        this.investmentRepository = investmentRepository;
        this.scpiRepository = scpiRepository;
        this.investmentMapper = investmentMapper;
        this.investorService = investorservice;
    }

    public List<InvestmentDto> getAllInvestments() {
        List<Investment> investments = investmentRepository.findAll();
        return investments.stream()
                .map(investmentMapper::toDTO)
                .toList();
    }

    public List<Investment> getInvestmentsByInvestorEmail(String email) {
        return investmentRepository.findByInvestorEmail(email);
    }

    public List<Investment> getInvestmentsByScpiId(Integer scpiId) {
        return investmentRepository.findByScpiId(scpiId);
    }

    public InvestmentDto saveInvestment(InvestmentDto investmentDto) throws RuntimeException {
        log.info("Début de la création d'un investissement pour SCPI ID: {}", investmentDto.getScpiId());
        Investment investment = investmentMapper.toEntity(investmentDto);
        Investor investor = investorService.getCurrentInvestor();
        log.info("Investisseur récupéré: {} {}", investor.getFirstName(), investor.getLastName());

        Scpi scpi = scpiRepository.findById(investmentDto.getScpiId())
                .orElseThrow(() -> {
                    log.error("SCPI non trouvée pour ID: {}", investmentDto.getScpiId());
                    return new RuntimeException("SCPI non trouvée");
                });
        investment.setInvestor(investor);
        investment.setScpi(scpi);
        investment.setInvestmentState("En cours");
        Investment savedInvestment = investmentRepository.save(investment);
        log.info("Investissement enregistré avec succès - ID: {}", savedInvestment.getId());
        return investmentMapper.toDTO(savedInvestment);
    }

}