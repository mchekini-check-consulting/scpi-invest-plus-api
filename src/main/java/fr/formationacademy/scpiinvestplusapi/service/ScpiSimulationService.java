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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ScpiSimulationService {

    private final ScpiSimulationRepository scpiSimulationRepository;
    private final ScpiSimulationMapper scpiSimulationMapper;
    private final ScpiRepository scpiRepository;
    private final SimulationRepository simulationRepository;

    @Autowired
    public ScpiSimulationService(ScpiSimulationRepository scpiSimulationRepository, ScpiSimulationMapper scpiSimulationMapper,
                                 ScpiRepository scpiRepository,
                                 SimulationRepository simulationRepository) {
        this.scpiSimulationRepository = scpiSimulationRepository;
        this.scpiSimulationMapper = scpiSimulationMapper;
        this.scpiRepository = scpiRepository;
        this.simulationRepository = simulationRepository;
    }

    public ScpiSimulationDToOut addScpiToSimulation(ScpiSimulationInDTO scpiSimulationInDTO) {

        log.info("AddScpiToSimulation - Get scpi by id: {}", scpiSimulationInDTO.getScpiId());
        Optional<Scpi> scpi = scpiRepository.findById(scpiSimulationInDTO.getScpiId());

        log.info("AddScpiToSimulation - Get simulation by id: {}", scpiSimulationInDTO.getSimulationId());
        Optional<Simulation> simulation = simulationRepository.findById(scpiSimulationInDTO.getSimulationId());
        ScpiSimulationId id = new ScpiSimulationId(scpiSimulationInDTO.getSimulationId(), scpiSimulationInDTO.getScpiId());
        if (scpiSimulationRepository.existsById(id)) {
            log.info("AddScpiToSimulation - The simulation with id {} already exists and has the scpi with id {}", id.getSimulationId(), id.getScpiId());
            return null;
        }
        if (scpi.isPresent() && simulation.isPresent()) {
            ScpiSimulation scpiSimulation = scpiSimulationMapper.toEntity(scpiSimulationInDTO);
            log.info("AddScpiToSimulation - Map ScpiSimulationInDto {} to ScpiSimulation {} ", scpiSimulationInDTO, scpiSimulation);

            scpiSimulation.setScpi(scpi.get());
            scpiSimulation.setSimulation(simulation.get());

            log.info("AddScpiToSimulation - Saving scpi simulation {}", scpiSimulation);
            ScpiSimulation savedScpiSimulation = scpiSimulationRepository.save(scpiSimulation);

            Simulation existingSimulation = simulation.get();

            if (existingSimulation.getScpiSimulations() == null) {
                log.info("AddScpiToSimulation - Init scpi simulation with an empty list");
                existingSimulation.setScpiSimulations(new ArrayList<>());
            }

            existingSimulation.getScpiSimulations().add(savedScpiSimulation);

            log.info("AddScpiToSimulation - Edited simulation {}", existingSimulation);
            simulationRepository.save(existingSimulation);
            return scpiSimulationMapper.toDTO(savedScpiSimulation);
        }
        return null;
    }


    public List<ScpiSimulationDToOut> getAllScpitSimulations() {
        return scpiSimulationMapper.toDTO(scpiSimulationRepository.findAll());
    }

    public ScpiSimulationDToOut deleteScipiFromSimulation(Integer simulationId, Integer scpiId) {
        Optional<ScpiSimulation> scpiSimulation = scpiSimulationRepository.findById(new ScpiSimulationId(simulationId, scpiId));
        if (scpiSimulation.isPresent()) {
            log.info("DeleteScipiFromSimulation - Simulation found with id  {}", simulationId);
            log.info("DeleteScipiFromSimulation - Scpi found with id  {}", scpiId);
            scpiSimulationRepository.deleteScpiFromSimulationById(new ScpiSimulationId(simulationId, scpiId));
            log.info("DeleteScipiFromSimulation - Deleted scpi simulation {}", scpiId);
            log.info("DeleteScipiFromSimulation - Simulation {} updated", simulationId);
            return scpiSimulationMapper.toDTO(scpiSimulation.get());
        }
        return null;
    }

}
