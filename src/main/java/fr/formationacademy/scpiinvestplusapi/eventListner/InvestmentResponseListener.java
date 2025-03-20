package fr.formationacademy.scpiinvestplusapi.eventListner;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentResponse;
import fr.formationacademy.scpiinvestplusapi.enums.InvestmentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_PARTNER_GROUP;
import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_PARTNER_RESPONSE_TOPIC;

@Component
public class InvestmentResponseListener {
    private static final Logger logger = LoggerFactory.getLogger(InvestmentResponseListener.class);

    @KafkaListener(topics = SCPI_PARTNER_RESPONSE_TOPIC, groupId = SCPI_PARTNER_GROUP)
    public void consumeResponse(InvestmentResponse response) {
        logger.info("Received investment response: {}", response);
        try {
            if (response != null) {
                logger.info("Réponse de la demande d'investissement reçu : {}", response);
                processResponse(response);
            } else {
                logger.error("Message Kafka vide ou mal formé");
            }
        } catch (Exception e) {
            logger.error("Erreur de traitement du message Kafka : {}", e.getMessage(), e);
        }
    }

    private void processResponse(InvestmentResponse response) {
        InvestmentState status = response.getInvestmentState();
        String investorEmail = response.getInvestorEmail();
        String scpiName = response.getScpiName();
        logger.info("Traitement du message : Status={}, InvestorEmail={}, ScpiName={}", status, investorEmail, scpiName);
    }
}
