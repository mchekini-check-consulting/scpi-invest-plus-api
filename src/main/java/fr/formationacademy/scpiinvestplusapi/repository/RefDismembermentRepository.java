package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;

import java.util.List;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefDismembermentRepository extends JpaRepository<RefDismemberment, Integer> {
    List<RefDismemberment> findByPropertyType(PropertyType type);

}
