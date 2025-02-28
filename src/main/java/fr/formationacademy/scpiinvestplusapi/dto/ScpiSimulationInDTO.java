package fr.formationacademy.scpiinvestplusapi.dto;

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
    private Integer simulationId;
    private Integer scpiId;
    private Integer numberPart;
    private BigDecimal partPrice;
    private BigDecimal rising;
    private Integer duree;
    private BigDecimal dureePercentage;
    private String propertyType;
}
