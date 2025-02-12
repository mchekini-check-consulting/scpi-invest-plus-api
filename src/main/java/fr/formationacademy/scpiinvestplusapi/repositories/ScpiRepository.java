package fr.formationacademy.scpiinvestplusapi.repositories;

import fr.formationacademy.scpiinvestplusapi.model.entiry.Scpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ScpiRepository extends JpaRepository<Scpi, Integer> {

    Optional<Scpi> findByName(String name);

    @Query("SELECT s FROM Scpi s WHERE s.name IN :names")
    Set<Scpi> findByNameIn(@Param("names") List<String> names);



}