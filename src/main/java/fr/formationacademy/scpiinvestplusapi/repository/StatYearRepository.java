package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatYearRepository  extends JpaRepository<StatYear, Integer> {
    List<StatYear> findByScpi_Id(Integer scpiId);
}
