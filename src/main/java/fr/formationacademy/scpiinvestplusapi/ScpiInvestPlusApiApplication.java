package fr.formationacademy.scpiinvestplusapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "fr.formationacademy.scpiinvestplusapi.repository")
public class ScpiInvestPlusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScpiInvestPlusApiApplication.class, args);
    }

}
