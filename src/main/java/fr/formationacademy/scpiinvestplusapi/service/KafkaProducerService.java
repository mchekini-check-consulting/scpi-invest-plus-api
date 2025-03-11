package fr.formationacademy.scpiinvestplusapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, InvestmentDtoOut> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, InvestmentDtoOut> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(String topic, InvestmentDtoOut message) throws JsonProcessingException {
        log.info("send - Message: {}", message);
        log.info("Sending message of type: {}", message.getClass().getName());
        String jsonMessage = objectMapper.writeValueAsString(message);
        log.info(" [x] Sending: {}", jsonMessage);
        log.info("Sending data to consumers: {}", jsonMessage);
        kafkaTemplate.send(topic, message);
    }
}
