package fr.formationacademy.scpiinvestplusapi.entity;

import java.math.BigDecimal;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private PropertyType typeProperty;
    private Integer numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;
    private String investmentState;
    private String investorId;
    @ManyToOne
    @JoinColumn(name = "scpi_id", nullable = false)
    private Scpi scpi;

}
