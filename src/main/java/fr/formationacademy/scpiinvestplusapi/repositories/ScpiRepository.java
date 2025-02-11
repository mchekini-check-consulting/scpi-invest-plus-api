package fr.formationacademy.scpiinvestplusapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.formationacademy.scpiinvestplusapi.dto.DetailsDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
@Repository
public interface ScpiRepository extends JpaRepository<Scpi, Integer>{
    @Query("SELECT new fr.formationacademy.scpiinvestplusapi.dto.DetailsDTO(s.name, s.subscriptionFees, s.managementCosts, sy.sharePrice, sy.reconstitutionValue, s.enjoymentDelay, s.scheduledPayment, sy.distributionRate) " +
   " FROM Scpi s " +
   " LEFT JOIN StatYear sy ON s.id = sy.scpi.id " +
   " WHERE s.id = :scpid")
DetailsDTO getScpiDetails(@Param("scpid") int scpid);



    
}
