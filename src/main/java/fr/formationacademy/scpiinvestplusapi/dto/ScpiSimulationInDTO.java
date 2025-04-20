package fr.formationacademy.scpiinvestplusapi.dto;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScpiSimulationInDTO {
    private Integer scpiId;
    private Integer numberPart;
    private BigDecimal statYear;
    private BigDecimal partPrice;
    private BigDecimal rising;
    private Integer duree;
    private BigDecimal dureePercentage;
    private BigDecimal grossRevenue;
    private BigDecimal netRevenue;
    private PropertyType propertyType;
}
