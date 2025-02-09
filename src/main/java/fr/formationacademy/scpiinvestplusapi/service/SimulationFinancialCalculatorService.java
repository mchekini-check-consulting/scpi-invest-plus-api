package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.FinancialResultDTO;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiSimulationInDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.ScpiSimulation;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.entity.TaxBracket;
import fr.formationacademy.scpiinvestplusapi.repository.TaxBracketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SimulationFinancialCalculatorService {

    private final TaxBracketRepository taxBracketRepository;

    public SimulationFinancialCalculatorService(TaxBracketRepository taxBracketRepository) {
        this.taxBracketRepository = taxBracketRepository;
    }

    @Transactional(readOnly = true)
    public FinancialResultDTO calculateFinancialResults(ScpiSimulationInDTO simulationInDTO, ScpiSimulation simulation) {

        Investor investor = simulation.getSimulation().getInvestor();
        BigDecimal fiscalParts = calculateFiscalParts(investor);
        BigDecimal annualIncome = BigDecimal.valueOf(investor.getAnnualIncome());
        BigDecimal quotientFamilial = annualIncome.divide(fiscalParts, RoundingMode.HALF_UP);
        log.info("Quotient Familial: {}", quotientFamilial);

        BigDecimal totalTax = calculateTotalTax(annualIncome.intValue(), fiscalParts);
        BigDecimal tmi = determineMarginalTaxRate(quotientFamilial);
        BigDecimal tm = calculateAverageTaxRate(totalTax, annualIncome);
        log.info("TMI: {}, TM: {}", tmi, tm);

        BigDecimal distributionRate = simulation.getScpi().getStatYears().stream()
                .max(Comparator.comparingInt(statYear -> statYear.getYearStat().getYearStat()))
                .map(StatYear::getDistributionRate)
                .orElse(BigDecimal.ZERO);

        BigDecimal grossRevenue = simulationInDTO.getPartPrice()
                .multiply(BigDecimal.valueOf(simulationInDTO.getNumberPart()))
                .multiply(
                        distributionRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                )
                .setScale(2, RoundingMode.HALF_UP);
        log.info("Gross Revenue: {}", grossRevenue);

        BigDecimal partFrance = simulation.getScpi().getLocations().stream()
                .filter(loc -> "France".equalsIgnoreCase(loc.getScpi().getName()))
                .map(Location::getCountryPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal partEurope = BigDecimal.valueOf(100).subtract(partFrance);
        log.info("Part France: {}, Part Europe: {}", partFrance, partEurope);

        BigDecimal netIncome = calculateNetIncome(grossRevenue, tmi, tm, partFrance, partEurope);
        log.info("Net Income: {}", netIncome);

        return new FinancialResultDTO(grossRevenue, netIncome);
    }


    private BigDecimal calculateTax(BigDecimal taxableIncome) {
        List<TaxBracket> brackets = taxBracketRepository.findAllByOrderByLowerBoundAsc();
        BigDecimal totalTax = BigDecimal.ZERO;
        log.info("Calcul de l'impôt pour un revenu imposable de: {}", taxableIncome);
        for (TaxBracket bracket : brackets) {
            if (taxableIncome.compareTo(bracket.getLowerBound()) <= 0) continue;
            BigDecimal taxableAmount = Optional.ofNullable(bracket.getUpperBound())
                    .map(taxableIncome::min)
                    .orElse(taxableIncome)
                    .subtract(bracket.getLowerBound())
                    .max(BigDecimal.ZERO);
            BigDecimal taxForBracket = taxableAmount.multiply(
                    bracket.getTaxRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
            );
            totalTax = totalTax.add(taxForBracket);
        }
        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal determineMarginalTaxRate(BigDecimal taxableIncome) {
        List<TaxBracket> brackets = taxBracketRepository.findAll();
        return brackets.stream()
                .filter(bracket -> taxableIncome.compareTo(bracket.getLowerBound()) >= 0)
                .map(TaxBracket::getTaxRate)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateAverageTaxRate(BigDecimal totalTax, BigDecimal taxableIncome) {
        return taxableIncome.compareTo(BigDecimal.ZERO) > 0
                ? totalTax.divide(taxableIncome, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
    }

    private BigDecimal calculateFiscalParts(Investor investor) {
        if (investor == null || investor.getMaritalStatus() == null || investor.getNumberOfChildren() == null) {
            log.error("Données incomplètes pour le calcul des parts fiscales.");
            throw new IllegalArgumentException("Données incomplètes pour le calcul des parts fiscales.");
        }

        BigDecimal parts;
        if (investor.getMaritalStatus().equalsIgnoreCase("married")) {
            parts = BigDecimal.valueOf(2);
        } else if (investor.getMaritalStatus().equalsIgnoreCase("single")) {
            parts = BigDecimal.ONE;
        } else {
            parts = BigDecimal.ONE;
        }

        int children = Integer.parseInt(investor.getNumberOfChildren());
        parts = parts.add(BigDecimal.valueOf(0.5 * Math.min(children, 2)))
                .add(BigDecimal.valueOf(Math.max(0, children - 2)));
        log.info("Nombre de parts fiscales calculé: {}", parts);
        return parts;
    }

    @Transactional(readOnly = true)
    protected BigDecimal calculateTotalTax(Integer taxableIncome, BigDecimal fiscalParts) {
        if (taxableIncome == null || taxableIncome <= 0 || fiscalParts == null || fiscalParts.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Revenu imposable ou parts fiscales incorrectes.");
            throw new IllegalArgumentException("Revenu imposable ou parts fiscales incorrectes.");
        }
        BigDecimal quotientFamilial = BigDecimal.valueOf(taxableIncome)
                .divide(fiscalParts, 2, RoundingMode.HALF_UP);
        log.info("Quotient familial calculé: {}", quotientFamilial);
        BigDecimal taxPerPart = calculateTax(quotientFamilial);
        BigDecimal totalTax = taxPerPart.multiply(fiscalParts).setScale(2, RoundingMode.HALF_UP);
        log.info("Impôt total calculé: {}", totalTax);
        return totalTax;
    }

    private BigDecimal calculateNetIncome(BigDecimal grossRevenue, BigDecimal tmi, BigDecimal tm,
                                          BigDecimal partFrance, BigDecimal partEurope) {
        if (grossRevenue == null || tmi == null || partFrance == null || partEurope == null) {
            log.error("Données manquantes pour le calcul du revenu net.");
            throw new IllegalArgumentException("Données manquantes pour le calcul du revenu net.");
        }
        BigDecimal cotisationsSociales = BigDecimal.valueOf(17.2);
        BigDecimal fractionFrance = partFrance.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal fractionEurope = partEurope.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal netFrance = grossRevenue.multiply(fractionFrance)
                .multiply(BigDecimal.ONE.subtract((tmi.add(cotisationsSociales))
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)));
        BigDecimal netEurope = grossRevenue.multiply(fractionEurope)
                .multiply(BigDecimal.ONE.subtract((tmi.subtract(tm))
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)));
        return netFrance.add(netEurope).setScale(2, RoundingMode.HALF_UP);
    }
}
