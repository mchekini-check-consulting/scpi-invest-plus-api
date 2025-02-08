package fr.formationacademy.scpiinvestplusapi.resource;

import fr.formationacademy.scpiinvestplusapi.model.ApplicationDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/application")
public class ApplicationResource {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String applicationVersion;


    @GetMapping
    public ApplicationDetails getApplicationDetails() {

        return ApplicationDetails.builder()
                .name(applicationName)
                .version(applicationVersion)
                .build();
    }


}
