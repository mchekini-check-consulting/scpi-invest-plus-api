package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScpiRepository extends JpaRepository<Scpi, Integer> {
    @Query("""
    SELECT s FROM Scpi s
    join Location loc on s.id = loc.id.scpiId\s
    join Sector sec on s.id = sec.id.scpiId\s
    join StatYear sy on s.id = sy.yearStat.scpiId 
    WHERE (:searchTerm IS NULL OR s.name ILIKE %:searchTerm%)
    AND ( :location IS NULL OR loc.id.country IN :location )
    AND (:minimumSubscription = 0 OR s.minimumSubscription >= :minimumSubscription)
    And (:sector IS NULL OR sec.id.name IN :sector )
    AND (:distributionRate IS NULL OR COALESCE(sy.distributionRate, 0) >= :distributionRate)
    AND ( :subscriptionFees IS NULL OR
            (:subscriptionFees = TRUE AND (s.subscriptionFees > 0 OR s.subscriptionFees IS NULL)) OR
            (:subscriptionFees = FALSE AND (s.subscriptionFees = 0 OR s.subscriptionFees IS NULL))
            )\s
              """)
    List<Scpi> searchScpi(String searchTerm, List<String> location, List<String> sector, double minimumSubscription,double distributionRate, Boolean subscriptionFees);
}
