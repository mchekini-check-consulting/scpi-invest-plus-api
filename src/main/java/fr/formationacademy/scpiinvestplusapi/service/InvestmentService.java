package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final ScpiRepository scpiRepository;
    private final InvestmentMapper investmentMapper;
    private final InvestorService investorService;
    private final InvestorRepository investorRepository;

    public InvestmentService(InvestmentRepository investmentRepository, ScpiRepository scpiRepository,
            InvestmentMapper investmentMapper, InvestorService investorservice, InvestorRepository investorRepository) {
        this.investmentRepository = investmentRepository;
        this.scpiRepository = scpiRepository;
        this.investmentMapper = investmentMapper;
        this.investorService = investorservice;
        this.investorRepository = investorRepository;

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


    public List<Investment> getInvestments(String email) {
        Optional<Investor> investor = investorRepository.findByEmail(email);
        if (investor.isEmpty()) {
            throw new RuntimeException("Investor does not exist");
        }
        return investmentRepository.findAllByInvestor(investor.get());
    }

}