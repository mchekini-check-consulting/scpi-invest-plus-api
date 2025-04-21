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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final SimulationMapper simulationMapper;
    private final ScpiSimulationMapper scpiSimulationMapper;
    private final ScpiRepository scpiRepository;
    private final UserService userService;

    @Autowired
    public SimulationService(SimulationRepository simulationRepository, SimulationMapper simulationMapper, ScpiSimulationMapper scpiSimulationMapper, ScpiRepository scpiRepository, UserService userService) {
        this.simulationRepository = simulationRepository;
        this.simulationMapper = simulationMapper;
        this.scpiSimulationMapper = scpiSimulationMapper;
        this.scpiRepository = scpiRepository;
        this.userService = userService;
    }


    public SimulationDToOut addSimulation(SimulationInDTO simulationInDTO) throws GlobalException {
        log.info("AddSimulation - Simulation body from request {}", simulationInDTO);
        String email = userService.getEmail();
        log.info("AddSimulation - Investor email already exists {} ", email);
        Simulation simulation = simulationMapper.toEntity(simulationInDTO);
        simulation.setInvestorEmail(email);
        List<ScpiSimulation> scpiSimulations = new ArrayList<>();
        for (ScpiSimulationInDTO scpiSimulationInDTO : simulationInDTO.getScpis()) {
            Scpi scpi = scpiRepository.findById(scpiSimulationInDTO.getScpiId()).orElseThrow(
                    () -> new GlobalException(HttpStatus.NOT_FOUND, "No scpi found with id: " + scpiSimulationInDTO.getScpiId())
            );
            ScpiSimulation scpiSimulation = scpiSimulationMapper.toEntity(scpiSimulationInDTO, simulation);
            scpiSimulation.setScpi(scpi);
            scpiSimulation.setSimulation(simulation);
            scpiSimulations.add(scpiSimulation);
        }

        simulation.setScpiSimulations(scpiSimulations);

        log.info("AddSimulation - Simulation has been added");
        simulationRepository.save(simulation);

        log.info("AddSimulation - Simulation created: {}", simulation);
        return simulationMapper.toDTO(simulation);
    }

    public List<SimulationDToOut> getSimulations() {
        String emailInvestor = userService.getEmail();
        List<Simulation> simulations = simulationRepository.findByInvestorEmail(emailInvestor);
        log.info("GetSimulations - Load the simulations {}", simulations);
        return simulationMapper.toDTO(simulations);
    }

    public SimulationDToOut getSimulationById(Integer simulationId) throws GlobalException {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "No simulation found with id: " + simulationId));
        return simulationMapper.toDTO(simulation);
    }


    public SimulationDToOut deleteSimulation(Integer simulationId) throws GlobalException {
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "No simulation found with id: " + simulationId));
        simulationRepository.deleteById(simulationId);
        return simulationMapper.toDTO(simulation);
    }
}
