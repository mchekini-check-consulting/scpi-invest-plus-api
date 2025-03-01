package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;

import org.springframework.stereotype.Service;

@Service
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

    public InvestmentDto saveInvestment(InvestmentDto investmentDto) throws RuntimeException {
        Investment investment = investmentMapper.toEntity(investmentDto);

        Investor investor = investorService.getCurrentInvestor();

        Scpi scpi = scpiRepository.findById(investmentDto.getScpiId())
                .orElseThrow(() -> new RuntimeException("SCPI non trouvée"));

        investment.setInvestor(investor);
        investment.setScpi(scpi);

        Investment savedInvestment = investmentRepository.save(investment);

        return investmentMapper.toDTO(savedInvestment);
    }

}