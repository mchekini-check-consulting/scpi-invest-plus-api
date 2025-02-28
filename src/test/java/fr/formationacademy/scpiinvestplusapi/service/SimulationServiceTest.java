package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import fr.formationacademy.scpiinvestplusapi.mapper.SimulationMapper;
import fr.formationacademy.scpiinvestplusapi.repository.SimulationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@Slf4j
@ExtendWith(MockitoExtension.class)
public class SimulationServiceTest {
    @Mock
    private InvestorService investorService;

    @Mock
    private SimulationMapper simulationMapper;

    @Mock
    private SimulationRepository simulationRepos;

    @InjectMocks
    private SimulationService simulationService;

    private SimulationInDTO simulationInDTO;
    private Investor investor;
    private Simulation simulation;
    private SimulationDToOut simulationDToOut;

    @BeforeEach
    void setUp() {
        simulationInDTO = new SimulationInDTO();
        simulationInDTO.setInvestorEmail("test@example.com");

        investor = new Investor();
        investor.setEmail("test@example.com");

        simulation = new Simulation();
        simulation.setInvestor(investor);

        simulationDToOut = new SimulationDToOut();
    }

    @Test
    void testAddSimulation_WhenInvestorExists() {
        // GIVEN
        when(investorService.getInvestorByEmail("test@example.com")).thenReturn(Optional.of(investor));
        when(simulationMapper.toEntity(simulationInDTO)).thenReturn(simulation);
        when(simulationRepos.save(simulation)).thenReturn(simulation);
        when(simulationMapper.toDTO(simulation)).thenReturn(simulationDToOut);

        // WHEN
        SimulationDToOut result = simulationService.addSimulation(simulationInDTO);


        // THEN
        assertNotNull(result);
        assertEquals(simulationDToOut, result);
        verify(investorService).getInvestorByEmail("test@example.com");
        verify(simulationMapper).toEntity(simulationInDTO);
        verify(simulationRepos).save(simulation);
        verify(simulationMapper).toDTO(simulation);
    }

    @Test
    void testAddSimulation_WhenInvestorDoesNotExist() {
        when(investorService.getInvestorByEmail("test@example.com")).thenReturn(Optional.empty());

        SimulationDToOut result = simulationService.addSimulation(simulationInDTO);

        assertNull(result);
        verify(investorService).getInvestorByEmail("test@example.com");
        verifyNoInteractions(simulationMapper, simulationRepos);
    }

    @Test
    void testDeleteSimulation_WhenSimulationExists() {
        Simulation simulation = new Simulation();
        simulation.setInvestor(investor);
        simulation.setId(1);

        when(simulationRepos.findById(1)).thenReturn(Optional.of(simulation));
        when(simulationMapper.toDTO(simulation)).thenReturn(simulationDToOut);
        SimulationDToOut result = simulationService.deleteSimulation(1);
        log.info("TestDelete - The simulation exist in database with id {} ", result);
        assertNotNull(result);
        assertEquals(simulationDToOut, result);


    }


}
