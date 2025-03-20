package fr.formationacademy.scpiinvestplusapi.eventListner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void consumeResponse(ConsumerRecord<String, String> record) {
        logger.info("Received investment response: {}", record);
        String jsonResponse = record.value();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> response = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
            logger.info("Réponse de la demande d'investissement reçu du topic {} : {}", SCPI_PARTNER_RESPONSE_TOPIC, response);
            processResponse(response);
        } catch (JsonProcessingException e) {
            logger.error("Erreur de parsing du message Kafka : {}", e.getMessage(), e);
        }
    }

    private void processResponse(Map<String, Object> response) {
        String status = (String) response.get("status");
        String investorEmail = (String) response.get("investorEmail");
        String scpiName = (String) response.get("scpiName");
        logger.info("Traitement du message : Status={}, InvestorEmail={}, ScpiName={}", status, investorEmail, scpiName);
    }
}
