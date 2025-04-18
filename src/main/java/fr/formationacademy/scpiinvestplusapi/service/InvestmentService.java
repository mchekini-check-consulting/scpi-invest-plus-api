package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.*;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.enums.InvestmentState;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentStatisticsDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.utils.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_REQUEST_TOPIC;

@Service
@Slf4j
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final ScpiService scpiService;
    private final RefDismembermentService refDismembermentService;
    private final InvestmentMapper investmentMapper;
    private final ScpiRepository scpiRepository;
    private final UserService userService;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public InvestmentService(InvestmentRepository investmentRepository, ScpiService scpiService,
                             InvestmentMapper investmentMapper, ScpiRepository scpiRepository, UserService userService,
                             KafkaTemplate<String, Object> kafkaTemplate, RefDismembermentService refDismembermentService) {
        this.investmentRepository = investmentRepository;
        this.scpiService = scpiService;
        this.investmentMapper = investmentMapper;
        this.scpiRepository = scpiRepository;
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
        this.refDismembermentService = refDismembermentService;
    }

    public InvestmentDto saveInvestment(InvestmentDto investmentDto) throws GlobalException {
        log.info("Début de la création d'un investissement.");

        if (investmentDto == null) {
            log.error("L'objet InvestmentDto est null.");
            throw new GlobalException(HttpStatus.BAD_REQUEST, "InvestmentDto ne peut pas être null.");
        }

        if (investmentDto.getScpiId() == null) {
            log.error("L'ID de la SCPI est null.");
            throw new GlobalException(HttpStatus.BAD_REQUEST, "L'ID de la SCPI ne peut pas être null.");
        }

        String email = userService.getEmail();
        log.info("Récupération de l'email de l'utilisateur : {}", email);

        ScpiDtoOut scpiDtoOut = scpiService.getScpiDetailsById(investmentDto.getScpiId());
        log.info("Détails SCPI récupérés : {}", scpiDtoOut);
        if (scpiDtoOut == null) {
            log.error("SCPI non trouvée pour ID: {}", investmentDto.getScpiId());
            throw new GlobalException(HttpStatus.NOT_FOUND, "Aucune SCPI trouvée avec l'ID: " + investmentDto.getScpiId());
        }
        log.info("SCPI trouvée : {} - {}", scpiDtoOut.getId(), scpiDtoOut.getName());

        Scpi scpiEntity = scpiRepository.findById(investmentDto.getScpiId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "SCPI non trouvée"));

        Investment investment = investmentMapper.toEntity(investmentDto);
        investment.setInvestorId(email);
        investment.setInvestmentState("En cours");
        investment.setScpi(scpiEntity);
        investment.setCreatedAt(LocalDate.now());


        Investment savedInvestment = investmentRepository.save(investment);
        log.info("Investissement enregistré avec succès - ID: {}", savedInvestment.getId());

        ScpiRequestDto request = ScpiRequestDto.builder()
                .investmentId(savedInvestment.getId())
                .scpiName(scpiDtoOut.getName())
                .amount(investmentDto.getTotalAmount())
                .propertyType(investmentDto.getTypeProperty())
                .numberYears(investmentDto.getNumberYears())
                .investorEmail(userService.getEmail())
                .build();
        log.info("Envoi la demande d'investissement au Bouchon pour Objet Traitement : {}", request);
        sendInvestment(request);
        log.info("Investissement envoyé avec succès à Kafka - ID: {}", savedInvestment.getId());

        return investmentMapper.toDTO(savedInvestment);
    }

    public void sendInvestment(ScpiRequestDto scpiRequestDto) {
        kafkaTemplate.send(SCPI_REQUEST_TOPIC, scpiRequestDto);
    }

    public List<InvestmentDtoOut> getInvestments(String state) {
        return investmentMapper.toDtoOutList(investmentRepository.findByInvestorIdAndInvestmentState(userService.getEmail(), state));
    }

    public Page<InvestmentDtoOut> getPageableInvestments(Pageable pageable, String state) {
        Page<Investment> investments;
        investments = investmentRepository.findByInvestorIdAndInvestmentState(userService.getEmail(), pageable, state);
        return investmentMapper.toDtoOutPage(investments);
    }


    public InvestmentStateDtoOut getPortfolio(Pageable pageable, String state) throws GlobalException {
        int totalInvestments = investmentRepository.countByInvestorId(userService.getEmail());
        Page<InvestmentDtoOut> investments = this.getPageableInvestments(pageable, state);

        return InvestmentStateDtoOut.builder()
                .totalInvesti(totalInvestments)
                .investments(investments)
                .build();
    }

    public InvestmentStatisticsDtoOut getInvestmentStats() throws GlobalException {
        List<InvestmentDtoOut> investmentsByState = this.getInvestments(InvestmentState.VALIDATED.toString());
        List<ScpiDtoOut> scpis = this.scpiService.getAllScpis();
        List<RefDismembermentDto> usuRefs = this.refDismembermentService.getByPropertyType(PropertyType.USUFRUIT);
        List<RefDismembermentDto> nueRefs = this.refDismembermentService.getByPropertyType(PropertyType.NUE_PROPRIETE);

        InvestmentStatisticsDtoOut statValues = Statistics.investmentPortfolioState(investmentsByState, scpis, usuRefs, nueRefs);

        return InvestmentStatisticsDtoOut.builder()
                .cashbackMontant(statValues.getCashbackMontant())
                .revenuMensuel(statValues.getRevenuMensuel())
                .rendementMoyen(statValues.getRendementMoyen())
                .montantInvesti(statValues.getMontantInvesti())
                .build();
    }
}
