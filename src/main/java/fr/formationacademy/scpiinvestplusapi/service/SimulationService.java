package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.*;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiSimulationMapper;
import fr.formationacademy.scpiinvestplusapi.mapper.SimulationMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiSimulationRepository;
import fr.formationacademy.scpiinvestplusapi.repository.SimulationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final InvestorService investorService;
    private final SimulationMapper simulationMapper;
    private final ScpiSimulationRepository scpiSimulationRepository;
    private final ScpiSimulationMapper scpiSimulationMapper;
    private final ScpiRepository scpiRepository;

    public SimulationService(SimulationRepository simulationRepository, InvestorService investorService, SimulationMapper simulationMapper, ScpiSimulationRepository scpiSimulationRepository, ScpiSimulationMapper scpiSimulationMapper, ScpiRepository scpiRepository) {
        this.simulationRepository = simulationRepository;
        this.investorService = investorService;
        this.simulationMapper = simulationMapper;
        this.scpiSimulationRepository = scpiSimulationRepository;
        this.scpiSimulationMapper = scpiSimulationMapper;
        this.scpiRepository = scpiRepository;
    }

    public SimulationDToOut addSimulation(SimulationInDTO simulationInDTO) throws GlobalException {
        log.info("AddSimulation - Simulation body from request {}", simulationInDTO);
        String email = simulationInDTO.getInvestorEmail();
        Investor investor = investorService.getInvestorByEmail(email).orElseThrow(
                () -> new GlobalException(HttpStatus.NOT_FOUND, "Investor with email " + email + " not found")
        );
        if (investor != null) {
            log.info("AddSimulation - Investor email already exists {} ", investor.getEmail());
            Simulation simulation = simulationMapper.toEntity(simulationInDTO);
            simulation.setInvestor(investor);
            simulation = simulationRepository.save(simulation);
            for (ScpiSimulationInDTO scpiSimulationInDTO : simulationInDTO.getScpis()) {
                try {
                    addScpiToSimulation(scpiSimulationInDTO, simulation);
                } catch (GlobalException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("AddSimulation - Simulation created: {}", simulation);
            return simulationMapper.toDTO(simulation);
        }
        return null;
    }

    public List<SimulationDToOut> getSimulations() {
        List<Simulation> simulations = simulationRepository.findAll();
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

    private void addScpiToSimulation(ScpiSimulationInDTO scpiSimulationInDTO, Simulation simulation) throws GlobalException {
        log.info("AddScpiToSimulation - Get scpi by id: {}", scpiSimulationInDTO.getScpiId());
        Scpi scpi = scpiRepository.findById(scpiSimulationInDTO.getScpiId()).orElseThrow(
                () -> new GlobalException(HttpStatus.NOT_FOUND, "No scpi found with id: " + scpiSimulationInDTO.getScpiId())
        );

        ScpiSimulationId id = new ScpiSimulationId(simulation.getId(), scpiSimulationInDTO.getScpiId());

        if (scpiSimulationRepository.existsById(id)) {
            return;
        }

        ScpiSimulation scpiSimulation = scpiSimulationMapper.toEntity(scpiSimulationInDTO);
        scpiSimulation.setScpi(scpi);
        scpiSimulation.setSimulation(simulation);

        scpiSimulation = scpiSimulationRepository.save(scpiSimulation);
        if (simulation.getScpiSimulations() == null) {
            simulation.setScpiSimulations(new ArrayList<>());
        }
        simulation.getScpiSimulations().add(scpiSimulation);
        simulationRepository.save(simulation);
        scpiSimulationMapper.toDTO(scpiSimulation);
    }
}
