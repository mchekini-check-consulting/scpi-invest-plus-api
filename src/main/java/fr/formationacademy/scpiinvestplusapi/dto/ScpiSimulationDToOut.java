package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScpiSimulationDToOut {
    private Integer simulationId;
    private Integer scpiId;
    private Integer numberPart;
    private BigDecimal partPrice;
    private BigDecimal rising;
    private Integer duree;
    private BigDecimal dureePercentage;
    private String propertyType;
    private String scpiName;
}
