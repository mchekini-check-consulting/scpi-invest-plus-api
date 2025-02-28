package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

import java.math.BigDecimal;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;

@Data
public class InvestmentDto {
    private Integer id;
    private PropertyType typeProperty;
    private Integer numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;
    private Integer scpiId;
}