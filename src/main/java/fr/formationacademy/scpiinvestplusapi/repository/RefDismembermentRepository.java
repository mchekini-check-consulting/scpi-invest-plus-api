package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefDismembermentRepository extends JpaRepository<RefDismemberment, Integer> {

    @Query("SELECT r FROM RefDismemberment r WHERE LOWER(r.propertyType) = LOWER(:typeProperty)")
    List<RefDismemberment> findByPropertyType(@Param("typeProperty") String typeProperty);
}
