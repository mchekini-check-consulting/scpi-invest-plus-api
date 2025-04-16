package fr.formationacademy.scpiinvestplusapi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentStatisticsDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;

public class Statistics {
    public static InvestmentStatisticsDtoOut investmentPortfolioState(List<InvestmentDtoOut> investments,
                                                                      List<ScpiDtoOut> scpis,
                                                                      List<RefDismembermentDto> usuRefs, List<RefDismembermentDto> nueRefs) {

        InvestmentStatisticsDtoOut result = InvestmentStatisticsDtoOut.builder().build();

        int montantInvesti = 0;
        double rendementMoyen = 0.0;
        int revenueMensuel = 0;
        double cashbackMontant = 0.0;

        //Caclul du Montant Investi
        montantInvesti = investments.stream()
                .mapToInt(inv -> inv.getTotalAmount().intValue())
                .sum();

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

        //Calcul des revenues mensuels
        for (InvestmentDtoOut inv : investments) {
            ScpiDtoOut scpi = scpis.stream()
                    .filter(sc -> sc.getName().equals(inv.getScpiName()))
                    .findFirst()
                    .orElse(null);

            if (scpi != null && scpi.getStatYear() != null) {
                int sharePrice = scpi.getStatYear().getSharePrice().intValue();
                int distributionRate = scpi.getStatYear().getDistributionRate().intValue();

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
                                    * ((sharePrice * 100) / ref.getRateDismemberment().intValue())
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
                                    * ((sharePrice * 100) / ref.getRateDismemberment().intValue())
                                    * distributionRate) / 12;
                        }
                    }
                }
            }
            // Cashback
            if (scpi != null && scpi.getCashback() != null) {
                double cashbackPourcent = scpi.getCashback().doubleValue();
                double cashback = (inv.getTotalAmount().doubleValue() * cashbackPourcent) / 100;
                cashbackMontant += cashback;
            }
        }

        result.setMontantInvesti(montantInvesti);
        result.setRendementMoyen(rendementMoyen);
        result.setRevenuMensuel(revenueMensuel);
        result.setCashbackMontant(cashbackMontant);

        return result;
    }
}