package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiSimulationMapper;
import fr.formationacademy.scpiinvestplusapi.mapper.SimulationMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.repository.SimulationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private SimulationMapper simulationMapper;

    @Mock
    private ScpiSimulationMapper scpiSimulationMapper;

    @Mock
    private ScpiRepository scpiRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SimulationService simulationService;

    private Simulation simulation;
    private SimulationInDTO simulationInDTO;
    private SimulationDToOut simulationDToOut;
    private Scpi scpi;
    private ScpiSimulation scpiSimulation;
    private ScpiSimulationInDTO scpiSimulationInDTO;

    @BeforeEach
    void setUp() {
        simulation = new Simulation();
        simulation.setId(1);
        simulation.setInvestorEmail("test@example.com");

        simulationInDTO = new SimulationInDTO();

        simulationDToOut = new SimulationDToOut();

        scpi = new Scpi();
        scpi.setId(1);

        scpiSimulation = new ScpiSimulation();

        scpiSimulationInDTO = new ScpiSimulationInDTO();
        scpiSimulationInDTO.setScpiId(1);

        simulationInDTO.setScpis(Collections.singletonList(scpiSimulationInDTO));
    }

    @Test
    void addSimulation_Success() throws GlobalException {
        when(userService.getEmail()).thenReturn("test@example.com");
        when(simulationMapper.toEntity(simulationInDTO)).thenReturn(simulation);
        when(scpiRepository.findById(1)).thenReturn(Optional.of(scpi));
        when(scpiSimulationMapper.toEntity(scpiSimulationInDTO, simulation)).thenReturn(scpiSimulation);
        when(simulationMapper.toDTO(simulation)).thenReturn(simulationDToOut);

        SimulationDToOut result = simulationService.addSimulation(simulationInDTO, true);

        assertNotNull(result);
        verify(simulationRepository, times(1)).save(simulation);
    }

    @Test
    void addSimulation_FailsWhenScpiNotFound() {
        when(userService.getEmail()).thenReturn("test@example.com");
        when(simulationMapper.toEntity(simulationInDTO)).thenReturn(simulation);
        when(scpiRepository.findById(1)).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> simulationService.addSimulation(simulationInDTO, true));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getSimulations_ReturnsList() {
        when(userService.getEmail()).thenReturn("test@example.com");
        when(simulationRepository.findByInvestorEmail("test@example.com")).thenReturn(Collections.singletonList(simulation));
        when(simulationMapper.toDTO(anyList())).thenReturn(Collections.singletonList(simulationDToOut));

        List<SimulationDToOut> results = simulationService.getSimulations();

        assertFalse(results.isEmpty());
        verify(simulationRepository, times(1)).findByInvestorEmail("test@example.com");
    }

    @Test
    void getSimulationById_Success() throws GlobalException {
        when(simulationRepository.findById(1)).thenReturn(Optional.of(simulation));
        when(simulationMapper.toDTO(simulation)).thenReturn(simulationDToOut);

        SimulationDToOut result = simulationService.getSimulationById(1);

        assertNotNull(result);
    }

    @Test
    void getSimulationById_FailsWhenNotFound() {
        when(simulationRepository.findById(1)).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> simulationService.getSimulationById(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void deleteSimulation_Success() throws GlobalException {
        when(simulationRepository.findById(1)).thenReturn(Optional.of(simulation));
        when(simulationMapper.toDTO(simulation)).thenReturn(simulationDToOut);

        SimulationDToOut result = simulationService.deleteSimulation(1);

        assertNotNull(result);
        verify(simulationRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteSimulation_FailsWhenNotFound() {
        when(simulationRepository.findById(1)).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> simulationService.deleteSimulation(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}
