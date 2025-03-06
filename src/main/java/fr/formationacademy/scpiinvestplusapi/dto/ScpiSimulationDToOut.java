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
public class ScpiSimulationDToOut {
    private Integer scpiId;
    private Integer numberPart;
    private BigDecimal partPrice;
    private BigDecimal rising;
    private Integer duree;
    private BigDecimal dureePercentage;
    private PropertyType propertyType;
    private String scpiName;
}
