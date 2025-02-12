package fr.formationacademy.scpiinvestplusapi.repositories;

import fr.formationacademy.scpiinvestplusapi.model.entiry.Sector;
import fr.formationacademy.scpiinvestplusapi.model.entiry.SectorKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SectorRepository extends JpaRepository<Sector, SectorKey> {
    List<Sector> findByScpiName(String name);
}
