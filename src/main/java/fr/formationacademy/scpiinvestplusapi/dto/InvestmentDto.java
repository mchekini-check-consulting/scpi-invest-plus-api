package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestmentDto {
    private String typeProperty;
    private Integer numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;
    private Integer scpiId;
    private String investmentState;
}
