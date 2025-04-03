package fr.formationacademy.scpiinvestplusapi.dto;

import java.math.BigDecimal;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import lombok.Data;

@Data
public class RefDismembermentDto {
    private PropertyType propertyType;
    private Integer yearDismemberment;
    private BigDecimal rateDismemberment;
}
