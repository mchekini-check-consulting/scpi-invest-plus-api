package fr.formationacademy.scpiinvestplusapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
@Repository
public interface ScpiRepository extends JpaRepository<Scpi, Integer>{
}





    

