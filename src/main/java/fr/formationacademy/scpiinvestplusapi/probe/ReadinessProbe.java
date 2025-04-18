package fr.formationacademy.scpiinvestplusapi.probe;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

@Component("customReadiness")
public class ReadinessProbe implements HealthIndicator {

    private final DataSource dataSource;
    private final String kafkaBootstrapServers;
    private final String keycloakIssuerUri;
    private final RestTemplate restTemplate = new RestTemplate();

    public ReadinessProbe(
            DataSource dataSource,
            @Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String keycloakIssuerUri
    ) {
        this.dataSource = dataSource;
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.keycloakIssuerUri = keycloakIssuerUri;
    }

    @Override
    public Health health() {
        Health.Builder builder = Health.up();

        try (Connection connection = dataSource.getConnection()) {
            if (connection == null || !connection.isValid(2)) {
                return Health.down().withDetail("database", "Not ready").build();
            }
            builder.withDetail("database", "READY");
        } catch (Exception e) {
            return Health.down().withDetail("database", "DOWN").withException(e).build();
        }

        try (AdminClient adminClient = AdminClient.create(kafkaProps())) {
            adminClient.listTopics().names().get();
            builder.withDetail("kafka", "READY");
        } catch (Exception e) {
            return Health.down().withDetail("kafka", "DOWN").withException(e).build();
        }

        try {
            restTemplate.getForEntity(keycloakIssuerUri, String.class);
            builder.withDetail("keycloak", "READY");
        } catch (Exception e) {
            return Health.down().withDetail("keycloak", "DOWN").withException(e).build();
        }

        return builder.build();
    }

    private Properties kafkaProps() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        return props;
    }
}
