package fr.formationacademy.scpiinvestplusapi.repositories;

import fr.formationacademy.scpiinvestplusapi.model.entiry.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, String> {

    @Query("SELECT i FROM Investor i WHERE i.email IN :emails")
    Set<Investor> findByEmailIn(@Param("emails") List<String> emails);


}
