package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class InvestmentDtoOut {
    private Integer id;
    private String typeProperty;
    private Integer numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;
    private String scpiName;
    private String investmentState;
    private LocalDate createdAt;
    private String rejectionReason;
}


