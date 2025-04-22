package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.entity.StatYearId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatYearRepository extends JpaRepository<StatYear, StatYearId> {
    List<StatYear> findByScpi_NameIn(List<String> scpiNames);
}
