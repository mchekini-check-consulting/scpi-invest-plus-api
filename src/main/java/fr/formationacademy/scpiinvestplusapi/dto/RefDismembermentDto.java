package fr.formationacademy.scpiinvestplusapi.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RefDismembermentDto {
    private Integer id;
    private Integer yearDismemberment;
    private BigDecimal rateDismemberment;
}
