package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.LocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, LocationId> {
    List<Location> findByScpi_NameIn(List<String> scpiNames);
}
