package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulationId;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiSimulationMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiSimulationRepository;
import fr.formationacademy.scpiinvestplusapi.repository.SimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScpiSimulationServiceTest {

    @Mock
    private ScpiSimulationRepository scpiSimulationRepository;

    @Mock
    private ScpiSimulationMapper scpiSimulationMapper;

    @Mock
    private ScpiRepository scpiRepository;

    @Mock
    private SimulationRepository simulationRepository;

    @InjectMocks
    private ScpiSimulationService scpiSimulationService;

    private ScpiSimulationInDTO scpiSimulationInDTO;
    private Scpi scpi;
    private Simulation simulation;
    private ScpiSimulation scpiSimulation;
    private ScpiSimulationDToOut scpiSimulationDToOut;

    @BeforeEach
    void setUp() {
        scpiSimulationInDTO = new ScpiSimulationInDTO();
        scpiSimulationInDTO.setScpiId(1);
        scpiSimulationInDTO.setSimulationId(2);

        scpi = new Scpi();
        simulation = new Simulation();
        scpiSimulation = new ScpiSimulation();
        scpiSimulationDToOut = new ScpiSimulationDToOut();
    }

    @Test
    void addScpiToSimulation_Success() {
        when(scpiRepository.findById(1)).thenReturn(Optional.of(scpi));
        when(simulationRepository.findById(2)).thenReturn(Optional.of(simulation));
        when(scpiSimulationMapper.toEntity(scpiSimulationInDTO)).thenReturn(scpiSimulation);
        when(scpiSimulationRepository.existsById(any(ScpiSimulationId.class))).thenReturn(false);
        when(scpiSimulationRepository.save(any(ScpiSimulation.class))).thenReturn(scpiSimulation);
        when(scpiSimulationMapper.toDTO(any(ScpiSimulation.class))).thenReturn(scpiSimulationDToOut);
        ScpiSimulationDToOut result = scpiSimulationService.addScpiToSimulation(scpiSimulationInDTO);

        assertNotNull(result);
        verify(scpiSimulationRepository, times(1)).save(any(ScpiSimulation.class));
    }


    @Test
    void addScpiToSimulation_Failure() {
        when(scpiRepository.findById(1)).thenReturn(Optional.empty());
        when(simulationRepository.findById(2)).thenReturn(Optional.empty());

        ScpiSimulationDToOut result = scpiSimulationService.addScpiToSimulation(scpiSimulationInDTO);

        assertNull(result);
        verify(scpiSimulationRepository, never()).save(any(ScpiSimulation.class));
    }

    @Test
    void getAllScpiSimulations_Success() {
        when(scpiSimulationRepository.findAll()).thenReturn(List.of(scpiSimulation));
        when(scpiSimulationMapper.toDTO(anyList())).thenReturn(List.of(scpiSimulationDToOut));

        List<ScpiSimulationDToOut> result = scpiSimulationService.getAllScpitSimulations();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void deleteScipiFromSimulation_Success() {
        ScpiSimulationId id = new ScpiSimulationId(2, 1);
        when(scpiSimulationRepository.findById(id)).thenReturn(Optional.of(scpiSimulation));
        when(scpiSimulationMapper.toDTO(scpiSimulation)).thenReturn(scpiSimulationDToOut);

        ScpiSimulationDToOut result = scpiSimulationService.deleteScipiFromSimulation(2, 1);

        assertNotNull(result);
        verify(scpiSimulationRepository, times(1)).deleteScpiFromSimulationById(id);
    }

    @Test
    void deleteScipiFromSimulation_Failure() {
        ScpiSimulationId id = new ScpiSimulationId(2, 1);
        when(scpiSimulationRepository.findById(id)).thenReturn(Optional.empty());

        ScpiSimulationDToOut result = scpiSimulationService.deleteScipiFromSimulation(2, 1);

        assertNull(result);
        verify(scpiSimulationRepository, never()).deleteScpiFromSimulationById(id);
    }
}
