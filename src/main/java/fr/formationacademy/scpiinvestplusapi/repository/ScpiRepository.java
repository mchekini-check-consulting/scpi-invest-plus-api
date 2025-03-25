package fr.formationacademy.scpiinvestplusapi.repository;


import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface
ScpiRepository extends JpaRepository<Scpi, Integer> {
    @Query("""
    SELECT s FROM Scpi s
    join Location loc on s.id = loc.id.scpiId
    join Sector sec on s.id = sec.id.scpiId
    join StatYear sy on s.id = sy.yearStat.scpiId 
    WHERE (:searchTerm IS NULL OR s.name ILIKE (%:searchTerm%))
    AND (:location IS NULL OR loc.id.country IN :location OR loc.id.country IS NULL)
    AND (:minimumSubscription IS NULL OR s.minimumSubscription >= :minimumSubscription)
    AND (:sector IS NULL OR sec.id.name IN :sector OR sec.id.name IS NULL)
    AND (:distributionRate IS NULL OR COALESCE(sy.distributionRate, 0) >= :distributionRate)
    AND (:subscriptionFees IS NULL OR
         (:subscriptionFees = TRUE AND (s.subscriptionFees > 0 OR s.subscriptionFees IS NULL)) OR
         (:subscriptionFees = FALSE AND (s.subscriptionFees = 0 OR s.subscriptionFees IS NULL))
    )
    """)
    List<Scpi> searchScpi(String searchTerm, List<String> location, List<String> sector, double minimumSubscription,double distributionRate, Boolean subscriptionFees);

    @Query("SELECT s FROM Scpi s WHERE s.name IN :names")
    Set<Scpi> findByNameIn(@Param("names") List<String> names);

    @Query("""
        SELECT s FROM Scpi s
        LEFT JOIN s.statYears sy
        WHERE sy.yearStat.yearStat = (
            SELECT MAX(sy2.yearStat.yearStat) FROM StatYear sy2 WHERE sy2.scpi = s
        ) OR sy.yearStat IS NULL
        ORDER BY sy.distributionRate DESC, sy.sharePrice DESC, sy.reconstitutionValue DESC
    """)
    List<Scpi> findAllOrderByLatestDistributionRateDesc();

}
