package fr.formationacademy.scpiinvestplusapi.repositories;

import fr.formationacademy.scpiinvestplusapi.model.entiry.Location;
import fr.formationacademy.scpiinvestplusapi.model.entiry.LocationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, LocationKey> {
    List<Location> findByScpiName(String name);
}
