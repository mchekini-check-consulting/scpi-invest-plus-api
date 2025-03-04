package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulationId;
import fr.formationacademy.scpiinvestplusapi.entity.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ScpiSimulationRepository extends JpaRepository<ScpiSimulation, ScpiSimulationId> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ScpiSimulation s WHERE s.scpiSimulationId = :scpiSimulationId ")
    void deleteScpiFromSimulationById(@Param("scpiSimulationId") ScpiSimulationId scpiSimulationId);
}
