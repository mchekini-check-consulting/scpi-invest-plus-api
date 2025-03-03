package fr.formationacademy.scpiinvestplusapi.repositories;

import fr.formationacademy.scpiinvestplusapi.entity.*;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiSimulationRepository;
import fr.formationacademy.scpiinvestplusapi.repository.SimulationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ScpiSimulationRepositoryTest {
    @Autowired
    private ScpiSimulationRepository scpiSimulationRepository;
    @Autowired
    private ScpiRepository scpiRepository;
    @Autowired
    private InvestorRepository investorRepository;
    @Autowired
    private SimulationRepository simulationRepository;
    @Test
    @Transactional
    void ScpiSimulationRepository_DeleteScpiFromSimulation_shouldReturnZero() {
        // Arrange
        Investor investor = Investor.builder()
                .email("test@gmail.com")
                .build();
        investor = investorRepository.save(investor);  // Save investor

        Scpi scpi = Scpi.builder()
                .name("test")
                .build();
        scpi = scpiRepository.save(scpi);
        scpiRepository.flush();

        Simulation simulation = Simulation.builder()
                .name("test")
                .simulationDate(LocalDate.now())
                .investor(investor)
                .build();
        simulation = simulationRepository.save(simulation);

        ScpiSimulation scpiSimulation = ScpiSimulation.builder()
                .scpiSimulationId(ScpiSimulationId.builder()
                        .simulationId(simulation.getId())
                        .scpiId(scpi.getId())
                        .build())
                .simulation(simulation)
                .scpi(scpi)
                .propertyType("propertyType")
                .duree(0)
                .dureePercentage(BigDecimal.valueOf(0.0))
                .numberPart(2)
                .partPrice(BigDecimal.valueOf(30))
                .build();
        scpiSimulationRepository.save(scpiSimulation);
        scpiSimulationRepository.flush();

        // Act
        scpiSimulationRepository.deleteScpiFromSimulationById(
                new ScpiSimulationId(simulation.getId(), scpi.getId()));

        // Assert
        Assertions.assertTrue(scpiSimulationRepository.findAll().isEmpty());
    }

}
