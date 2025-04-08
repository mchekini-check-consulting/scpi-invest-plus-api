package fr.formationacademy.scpiinvestplusapi.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import fr.formationacademy.scpiinvestplusapi.enums.InvestmentStatus;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;
    private PropertyType typeProperty;
    private Integer numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;
    private String investmentState;
    private String investorId;
    private String rejectedReason;
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "scpi_id", nullable = false)
    private Scpi scpi;

}
