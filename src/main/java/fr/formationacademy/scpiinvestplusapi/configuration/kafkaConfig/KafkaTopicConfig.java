package fr.formationacademy.scpiinvestplusapi.configuration.kafkaConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.SCPI_REQUEST_TOPIC;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic getTopic() {
        return TopicBuilder.name(SCPI_REQUEST_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
