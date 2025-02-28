package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefDismembermentRepository extends JpaRepository<RefDismemberment, String> {
    List<RefDismemberment> findByPropertyType(PropertyType typeProperty);

}
