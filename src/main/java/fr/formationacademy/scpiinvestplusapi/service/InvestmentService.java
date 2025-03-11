package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final ScpiRepository scpiRepository;
    private final InvestmentMapper investmentMapper;
    private final UserService userService;

    public InvestmentService(InvestmentRepository investmentRepository, ScpiRepository scpiRepository,
                             InvestmentMapper investmentMapper, UserService userService, InvestorRepository investorRepository) {
        this.investmentRepository = investmentRepository;
        this.scpiRepository = scpiRepository;
        this.investmentMapper = investmentMapper;
        this.userService = userService;

    }

    public InvestmentDto saveInvestment(InvestmentDto investmentDto) throws GlobalException {
        log.info("Début de la création d'un investissement pour SCPI ID: {}", investmentDto.getScpiId());
        Investment investment = investmentMapper.toEntity(investmentDto);
        String email = userService.getEmail();
        Scpi scpi = scpiRepository.findById(investmentDto.getScpiId())
                .orElseThrow(() -> {
                    log.error("SCPI non trouvée pour ID: {}", investmentDto.getScpiId());
                    return new  GlobalException(HttpStatus.NOT_FOUND,"No SCPI found with id: " + investmentDto.getScpiId());
                });
        investment.setInvestorId(email);
        investment.setScpi(scpi);
        investment.setInvestmentState("En cours");
        Investment savedInvestment = investmentRepository.save(investment);
        log.info("Investissement enregistré avec succès - ID: {}", savedInvestment.getId());
        return investmentMapper.toDTO(savedInvestment);
    }

    public List<InvestmentDtoOut> getInvestments() {
        return  investmentMapper.toDtoOutList(investmentRepository.findByInvestorId(userService.getEmail()));
    }


}