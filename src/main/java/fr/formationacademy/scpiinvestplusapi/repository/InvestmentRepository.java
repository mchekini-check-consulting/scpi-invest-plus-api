package fr.formationacademy.scpiinvestplusapi.repository;

import fr.formationacademy.scpiinvestplusapi.entity.Investment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {
    int countByInvestorId(String investmentEmail);
    List<Investment> findByInvestorIdAndInvestmentState(String investmentEmail, String state);
    Page<Investment> findByInvestorIdAndInvestmentState(String investorId, Pageable pageable, String investmentState);
}

