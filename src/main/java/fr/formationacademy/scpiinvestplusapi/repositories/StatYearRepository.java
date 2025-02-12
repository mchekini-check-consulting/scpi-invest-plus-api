package fr.formationacademy.scpiinvestplusapi.repositories;


import fr.formationacademy.scpiinvestplusapi.model.entiry.StatYear;
import fr.formationacademy.scpiinvestplusapi.model.entiry.StatYearKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatYearRepository extends JpaRepository<StatYear, StatYearKey> {
    List<StatYear> findByScpiName(String name);
}
