package fr.formationacademy.scpiinvestplusapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.service.KafkaProducerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Kafka", description = "Points d'accès pour la gestion des plans d'abonnement")
@Slf4j
@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaProducerRessource {
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public KafkaProducerRessource(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody InvestmentDtoOut message) throws JsonProcessingException {
        log.info("sendMessage - Message: {}", message);
        kafkaProducerService.send("scpi-partner-topic", message);
        log.info("sendMessage - Message sent : {}", message);
        return ResponseEntity.ok("Message envoyé: " + message);
    }
}
