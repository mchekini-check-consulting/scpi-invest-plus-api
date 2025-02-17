package fr.formationacademy.scpiinvestplusapi.repository;


import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.entity.StatYearId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatYearRepository extends JpaRepository<StatYear, StatYearId> {
}
