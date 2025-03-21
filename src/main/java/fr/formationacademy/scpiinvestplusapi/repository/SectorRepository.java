package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.SectorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, SectorId> {
    @Query("SELECT s FROM Sector s WHERE s.id.scpiId = :scpiId")
    List<Sector> findByScpiId(@Param("scpiId") Integer scpiId);
}
