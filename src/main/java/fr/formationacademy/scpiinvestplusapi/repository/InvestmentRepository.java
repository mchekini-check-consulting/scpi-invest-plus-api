package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Investment;

import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, String> {

    List<Investment> findAllByInvestor(Investor investor);

}
