package fr.formationacademy.scpiinvestplusapi.eventListner;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentResponse;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.utils.TopicNameProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InvestmentResponseListener {

    private final InvestmentRepository investmentRepository;
    private final TopicNameProvider topicNameProvider;

    public InvestmentResponseListener(InvestmentRepository investmentRepository, TopicNameProvider topicNameProvider) {
        this.investmentRepository = investmentRepository;
        this.topicNameProvider = topicNameProvider;
    }

    @KafkaListener(topics = "#{topicNameProvider.getScpiInvestPartnerResponseTopic()}", groupId = "#{topicNameProvider.getGroupTopic()}")
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
        log.info("[processResponse] Modification de l'état de la demande dans postgresql selon la réponse reçu du partenaire : {}", response);
        investmentRepository.findById(response.getInvestmentId()).ifPresent(investment -> {
            investment.setInvestmentState(response.getInvestmentState().toString());
            investment.setRejectedReason(response.getRejectionReason());
            investmentRepository.save(investment);
        });

    }
}
