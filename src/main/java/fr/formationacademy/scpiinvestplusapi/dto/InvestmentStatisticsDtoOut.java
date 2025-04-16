package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvestmentStatisticsDtoOut {
    public int montantInvesti;
    public double rendementMoyen;
    public int revenuMensuel;
    public double cashbackMontant;
}