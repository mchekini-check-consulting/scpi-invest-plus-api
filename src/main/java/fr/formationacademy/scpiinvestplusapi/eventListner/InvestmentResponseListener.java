package fr.formationacademy.scpiinvestplusapi.eventListner;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_PARTNER_GROUP;
import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_PARTNER_RESPONSE_TOPIC;

@Component
public class InvestmentResponseListener {
    private static final Logger logger = LoggerFactory.getLogger(InvestmentResponseListener.class);

    @KafkaListener(topics = SCPI_PARTNER_RESPONSE_TOPIC, groupId = SCPI_PARTNER_GROUP)
    public void consumeResponse(ConsumerRecord<String, Map<String, Object>> record) {
        logger.info("Received investment response: {}", record);
        Map<String, Object> response = record.value();
        logger.info("Réponse de la demande d'investissement reçu du topic {} : {}", SCPI_PARTNER_RESPONSE_TOPIC, response);
        processResponse(response);
    }

    private void processResponse(Map<String, Object> response) {
        String status = (String) response.get("status");
        String investorEmail = (String) response.get("investorEmail");
        String scpiName = (String) response.get("scpiName");

        logger.info("Traitement du message : Status={}, InvestorEmail={}, ScpiName={}", status, investorEmail, scpiName);

        // Ajoute ici la suite du traitement (enregistrement en base, notification, etc.)
    }
}
