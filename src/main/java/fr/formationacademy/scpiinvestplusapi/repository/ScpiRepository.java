package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScpiRepository extends JpaRepository<Scpi, Integer>{
//    @Query("SELECT new fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO(s.id, s.name, s.minimumSubscription, s.manager, s.capitalization, " +
//            "s.subscriptionFees, s.managementCosts, s.enjoymentDelay, s.iban, s.bic, s.scheduledPayment, " +
//            "s.frequencyPayment, s.cashback, s.advertising) " +
//            "FROM Scpi s " +
//            "LEFT JOIN StatYear sy ON s.id = sy.scpi.id " +
//            "WHERE s.id = :scpid")
//
//    ScpiDTO getScpiDetails(@Param("scpid") Integer scpid);

    Optional<Scpi> findById(Integer id);



}





    

