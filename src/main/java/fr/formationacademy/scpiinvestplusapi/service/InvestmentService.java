package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiRequestDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_REQUEST_TOPIC;

@Service
@Slf4j
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final ScpiService scpiService;
    private final InvestmentMapper investmentMapper;
    private final ScpiRepository scpiRepository;
    private final UserService userService;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public InvestmentService(InvestmentRepository investmentRepository, ScpiService scpiService,
                             InvestmentMapper investmentMapper, ScpiRepository scpiRepository, UserService userService,
                             KafkaTemplate<String, Object> kafkaTemplate) {
        this.investmentRepository = investmentRepository;
        this.scpiService = scpiService;
        this.investmentMapper = investmentMapper;
        this.scpiRepository = scpiRepository;
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;

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

    public List<InvestmentDtoOut> getInvestments() {
        return investmentMapper.toDtoOutList(investmentRepository.findByInvestorId(userService.getEmail()));
    }

    public void sendInvestment(ScpiRequestDto scpiRequestDto) {
        kafkaTemplate.send(SCPI_REQUEST_TOPIC, scpiRequestDto);
    }

}