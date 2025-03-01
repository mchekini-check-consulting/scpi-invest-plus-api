package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

import java.math.BigDecimal;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;

@Data
public class InvestmentDto {
    private PropertyType typeProperty;
    private BigDecimal numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;
    private Integer scpiId;
}