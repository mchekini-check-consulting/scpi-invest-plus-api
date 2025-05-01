package fr.formationacademy.scpiinvestplusapi.eventListner;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentResponse;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_PARTNER_GROUP;
import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_PARTNER_RESPONSE_TOPIC;

@Component
@Slf4j
public class InvestmentResponseListener {

    private final InvestmentRepository investmentRepository;

    public InvestmentResponseListener(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    @KafkaListener(topics = SCPI_PARTNER_RESPONSE_TOPIC, groupId = SCPI_PARTNER_GROUP)
    public void consumeResponse(InvestmentResponse response) {
        log.info("Received investment response: {}", response);
        try {
            if (response != null) {
                log.info("Réponse de la demande d'investissement reçu : {}", response);
                processResponse(response);
            } else {
                log.error("Message Kafka vide ou mal formé");
            }
        } catch (Exception e) {
            log.error("Erreur de traitement du message Kafka : {}", e.getMessage(), e);
        }
    }

    private void processResponse(InvestmentResponse response) {
        investmentRepository.findById(response.getInvestmentId()).ifPresent(investment -> {
            investment.setInvestmentState(response.getInvestmentState().toString());
            investment.setRejectedReason(response.getRejectionReason());
            log.info("The new State is assigned to {} ", investment.getId(), " with value : ", investment.getInvestmentState());
            investmentRepository.save(investment);
        });
    }
}
