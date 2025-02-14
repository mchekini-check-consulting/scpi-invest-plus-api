package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Scpi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScpiRepository extends JpaRepository<Scpi, Integer> {
    @Query("SELECT s FROM Scpi s WHERE LOWER(s.name) LIKE LOWER(CONCAT(:searchItem, '%'))")
    List<Scpi> searchScpisByName(@Param("searchItem") String searchItem);
}
