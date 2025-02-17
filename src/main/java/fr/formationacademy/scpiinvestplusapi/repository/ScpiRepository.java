package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static fr.formationacademy.scpiinvestplusapi.utils.Constants.IBAN_PATTERN;
import static fr.formationacademy.scpiinvestplusapi.utils.Constants.INVALID_IBAN;

@Repository
public interface ScpiRepository  extends JpaRepository<Scpi, Integer> {
    @Query("SELECT s FROM Scpi s WHERE s.name IN :names")
    Set<Scpi> findByNameIn(@Param("names") List<String> names);

    Optional<Scpi> findByIban(@Pattern(regexp = IBAN_PATTERN , message = INVALID_IBAN) String iban);
}
