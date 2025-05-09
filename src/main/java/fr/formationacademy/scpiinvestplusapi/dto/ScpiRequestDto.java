package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScpiRequestDto {
    private Integer investmentId;
    private String scpiName;
    private BigDecimal amount;
    private String investorEmail;
    private PropertyType propertyType;
    private Integer numberYears;
}
