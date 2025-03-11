package fr.formationacademy.scpiinvestplusapi.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic getTopic() {
        return TopicBuilder.name("scpi-partner-topic")
                .partitions(1)
                .replicas(3)
                .build();
    }
}
