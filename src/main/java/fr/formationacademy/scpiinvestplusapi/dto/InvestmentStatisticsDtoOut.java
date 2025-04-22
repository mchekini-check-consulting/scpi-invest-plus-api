package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InvestmentStatisticsDtoOut {
    public int montantInvesti;
    public double rendementMoyen;
    public int revenuMensuel;
    public double cashbackMontant;
    private Map<String, Double> repGeographique;
    private Map<String, Double> repSectoriel;
    private Map<String, Double> distributionHistory;
}