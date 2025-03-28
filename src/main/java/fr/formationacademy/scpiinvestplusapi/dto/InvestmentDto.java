package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestmentDto {
    private PropertyType typeProperty;
    private Integer numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;
    private Integer scpiId;
    private String investmentState;
}
