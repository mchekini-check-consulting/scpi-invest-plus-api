package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.SectorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SectorRepository extends JpaRepository<Sector, SectorId> {
    List<Sector> findByScpi_NameIn(List<String> scpiNames);
}
