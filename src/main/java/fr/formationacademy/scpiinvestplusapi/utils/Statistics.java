package fr.formationacademy.scpiinvestplusapi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentStatisticsDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;

public class Statistics {
    public static InvestmentStatisticsDtoOut investmentPortfolioState(List<InvestmentDtoOut> investments,
                                                                      List<ScpiDtoOut> scpis,
                                                                      List<RefDismembermentDto> usuRefs, List<RefDismembermentDto> nueRefs,
                                                                      List<Location> locations,
                                                                      List<Sector> sectors,
                                                                      List<StatYear> statYears) {

        InvestmentStatisticsDtoOut result = InvestmentStatisticsDtoOut.builder().build();

        double montantInvesti = 0.0;
        double rendementMoyen = 0.0;
        double revenueMensuel = 0.0;
        double cashbackMontant = 0.0;
        Map<String, Double> repGeographique = new HashMap<>();
        Map<String, Double> repSectoriel = new HashMap<>();
        Map<String, Double> distributionHistory = new HashMap<>();

        //Caclul du Montant Investi
        montantInvesti = investments.stream()
                .mapToDouble(inv -> inv.getTotalAmount().doubleValue())
                .sum();
        montantInvesti = BigDecimal.valueOf(montantInvesti)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        //Calucul du Rendement Moyen
        DoubleStream tauxDistributionStream = investments.stream()
                .map(inv -> scpis.stream()
                        .filter(scpi -> scpi.getName().equals(inv.getScpiName()))
                        .findFirst()
                        .map(scpi -> scpi.getStatYear() != null ? scpi.getStatYear().getDistributionRate() : null)
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue);

        OptionalDouble moyenneOptional = tauxDistributionStream.average();
        rendementMoyen = moyenneOptional.isPresent() ?
                BigDecimal.valueOf(moyenneOptional.getAsDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0;

        //Calcul des Revenues Mensuels
        for (InvestmentDtoOut inv : investments) {
            ScpiDtoOut scpi = scpis.stream()
                    .filter(sc -> sc.getName().equals(inv.getScpiName()))
                    .findFirst()
                    .orElse(null);

            if (scpi != null && scpi.getStatYear() != null) {
                double sharePrice = scpi.getStatYear().getSharePrice().doubleValue();
                double distributionRate = scpi.getStatYear().getDistributionRate().doubleValue() / 100 ;

                if (inv.getTypeProperty().equals(PropertyType.PLEINE_PROPRIETE.toString())) {
                    revenueMensuel += (inv.getNumberShares() * sharePrice * distributionRate) / 12;
                }

                if (inv.getTypeProperty().equals(PropertyType.NUE_PROPRIETE.toString())) {
                    long yearDiff = ChronoUnit.YEARS.between(inv.getCreatedAt(), LocalDate.now());

                    if (yearDiff >= inv.getNumberYears()) {
                        RefDismembermentDto ref = nueRefs.stream()
                                .filter(n -> n.getYearDismemberment() == inv.getNumberYears())
                                .findFirst()
                                .orElse(null);


                        if (ref != null) {
                            revenueMensuel += (inv.getNumberShares()
                                    * ((sharePrice * 100) / (ref.getRateDismemberment().doubleValue() / 100))
                                    * distributionRate) / 12;
                        }
                    }
                }

                if (inv.getTypeProperty().equals(PropertyType.USUFRUIT.toString())) {
                    long yearDiff = ChronoUnit.YEARS.between(inv.getCreatedAt(), LocalDate.now());

                    if (yearDiff <= inv.getNumberYears()) {
                        RefDismembermentDto ref = usuRefs.stream()
                                .filter(n -> n.getYearDismemberment() == inv.getNumberYears())
                                .findFirst()
                                .orElse(null);

                        if (ref != null) {
                            revenueMensuel += (inv.getNumberShares()
                                    * ((sharePrice * (ref.getRateDismemberment().doubleValue() / 100)) / 100 )
                                    * distributionRate) / 12;
                        }
                    }
                }
            }
            revenueMensuel = BigDecimal.valueOf(revenueMensuel)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            // Cashback
            if (scpi != null && scpi.getCashback() != null) {
                double cashbackPourcent = scpi.getCashback().doubleValue();
                double cashback = (inv.getTotalAmount().doubleValue() * cashbackPourcent) / 100;
                cashbackMontant += cashback;
            }
        }

        // Rep geographique
        Map<String, Double> repartitionGeo = new HashMap<>();

        for (Location location : locations) {
            String country = location.getId().getCountry();
            String scpiName = location.getScpi().getName();
            BigDecimal countryPercentage = location.getCountryPercentage();

            investments.stream()
                    .filter(investment -> investment.getScpiName().equals(scpiName))
                    .findFirst()
                    .ifPresent(investment -> {
                        BigDecimal totalAmount = investment.getTotalAmount();
                        double totalInvestiParPays = countryPercentage.doubleValue() * totalAmount.doubleValue() / 100;

                        repartitionGeo.merge(country, totalInvestiParPays, Double::sum);
                    });
        }

        Map<String, Double> repartitionPourcent = new HashMap<>();
        for (Map.Entry<String, Double> entry : repartitionGeo.entrySet()) {
            double pourcentage = (entry.getValue() / montantInvesti) * 100.0;
            double arrondi = Math.round(pourcentage * 100.0) / 100.0;
            repartitionPourcent.put(entry.getKey(), arrondi);
        }
        repGeographique = repartitionPourcent;


        //Rep Sectoriel:
        Map<String, Double> repartitionSectorielle = new HashMap<>();

        for (Sector sector : sectors) {
            String secteur = sector.getId().getName();
            String scpiName = sector.getScpi().getName();
            BigDecimal sectorPercentage = sector.getSectorPercentage();

            investments.stream()
                    .filter(investment -> investment.getScpiName().equals(scpiName))
                    .findFirst()
                    .ifPresent(investment -> {
                        BigDecimal totalAmount = investment.getTotalAmount();
                        double montantInvestiDansSecteur = sectorPercentage.doubleValue() * totalAmount.doubleValue() / 100.0;

                        repartitionSectorielle.merge(secteur, montantInvestiDansSecteur, Double::sum);
                    });
        }

        Map<String, Double> repartitionPourcentSectorielle = new HashMap<>();
        for (Map.Entry<String, Double> entry : repartitionSectorielle.entrySet()) {
            double pourcentage = (entry.getValue() / montantInvesti) * 100.0;
            double arrondi = Math.round(pourcentage * 100.0) / 100.0;
            repartitionPourcentSectorielle.put(entry.getKey(), arrondi);
        }
        repSectoriel = repartitionPourcentSectorielle;


        // TotalDistribution
        Map<String, Double> totalDistribution = new HashMap<>();

        Map<Integer, List<StatYear>> statYearsByYear = statYears.stream()
                .filter(stat -> investments.stream().anyMatch(inv -> inv.getScpiName().equals(stat.getScpi().getName())))
                .collect(Collectors.groupingBy(stat -> stat.getYearStat().getYearStat()));


        for (Map.Entry<Integer, List<StatYear>> entry : statYearsByYear.entrySet()) {
            int year = entry.getKey();
            List<StatYear> stats = entry.getValue();

            double totalDistribueAnnee = 0.0;

            for (StatYear stat : stats) {
                String scpiName = stat.getScpi().getName();

                Optional<InvestmentDtoOut> matchedInvestment = investments.stream()
                        .filter(investment -> investment.getScpiName().equals(scpiName))
                        .findFirst();

                if (matchedInvestment.isPresent() && stat.getDistributionRate() != null) {
                    double taux = stat.getDistributionRate().doubleValue();
                    double montantInvestiScpi = matchedInvestment.get().getTotalAmount().doubleValue();

                    totalDistribueAnnee += (taux * montantInvestiScpi) / 100.0;
                }
            }

            double tauxPondere = (totalDistribueAnnee / montantInvesti) * 100.0;
            double tauxArrondi = Math.round(tauxPondere * 100.0) / 100.0;

            totalDistribution.put(String.valueOf(year), tauxArrondi);
        }
        distributionHistory = totalDistribution;

        result.setMontantInvesti(montantInvesti);
        result.setRendementMoyen(rendementMoyen);
        result.setRevenuMensuel(revenueMensuel);
        result.setCashbackMontant(cashbackMontant);
        result.setRepGeographique(repGeographique);
        result.setRepSectoriel(repSectoriel);
        result.setDistributionHistory(distributionHistory);

        return result;
    }
}