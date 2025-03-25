package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.model.ApplicationDetails;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "application")
@Slf4j
public class ApplicationResource {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String applicationVersion;

    private final Counter counter;

    public ApplicationResource(MeterRegistry meterRegistry) {
        this.counter = Counter.builder("application_details_counter")
                .description("métrique qui calcule le nombre d'appel")
                .register(meterRegistry);
    }


    @GetMapping("/details")
    public ApplicationDetails getApplicationDetails() {

        counter.increment();

        log.info("Application : {} version : {} is UP", applicationName, applicationVersion);

        return ApplicationDetails.builder()
                .name(applicationName)
                .version(applicationVersion)
                .build();
    }
}
