package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.SimulationDToOut;
import fr.formationacademy.scpiinvestplusapi.dto.SimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import fr.formationacademy.scpiinvestplusapi.mapper.SimulationMapper;
import fr.formationacademy.scpiinvestplusapi.repository.SimulationRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Builder
@Service
@RequiredArgsConstructor
@Slf4j
public class SimulationService {
    private SimulationRepository simulationRepository;
    private InvestorService investorService;
    private SimulationMapper simulationMapper;

    @Autowired
    public SimulationService(SimulationRepository simulationRepository, InvestorService investorService, SimulationMapper simulationMapper) {
        this.simulationRepository = simulationRepository;
        this.investorService = investorService;
        this.simulationMapper = simulationMapper;
    }

    public SimulationDToOut addSimulation(SimulationInDTO simulationInDTO) {
        log.info("AddSimulation - Simulation body from request {}", simulationInDTO);
        String email = simulationInDTO.getInvestorEmail();
        Optional<Investor> investor = investorService.getInvestorByEmail(email);
        if (investor.isPresent()) {
            log.info("AddSimulation - Investor email already exists {} ", investor.get().getEmail());
            Simulation simulation = simulationMapper.toEntity(simulationInDTO);
            log.info("AddSimulation - SimulationDTO mapped to entity  {}", simulation);
            log.info("Investor investor {}", investor.get());
            simulation.setInvestor(investor.get());
            log.info("AddSimulation - Investor in simulation created {}", simulation);
            return simulationMapper.toDTO(simulationRepository.save(simulation));
        }
        return null;
    }

    public List<SimulationDToOut> getSimulations() {
        List<Simulation> simulations = simulationRepository.findAll();
        log.info("GetSimulations - Load the simulations  {}", simulations);
        List<SimulationDToOut> simulationsDTO = simulationMapper.toDTO(simulations);
        log.info("GetSimulations - Load the simulations dto {}", simulationsDTO);
        return simulationsDTO;
    }

    public SimulationDToOut deleteSimulation(Integer simulationId) {
        Optional<Simulation> simulation = simulationRepository.findById(simulationId);

        if (simulation.isPresent()) {
            log.info("DeleteSimulation - The simulation exist in database with id {} ", simulation.get().getId());
            simulationRepository.deleteById(simulationId);
            log.info("DeleteSimulation - The simulation was deleted {}", simulation);
            return simulationMapper.toDTO(simulation.get());
        }
        log.info("DeleteSimulation - The simulation with id {} not found", simulationId);
        return null;
    }
}
