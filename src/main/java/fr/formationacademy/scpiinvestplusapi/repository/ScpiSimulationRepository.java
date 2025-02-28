package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScpiSimulationRepository extends JpaRepository<ScpiSimulation, Integer> {
}
