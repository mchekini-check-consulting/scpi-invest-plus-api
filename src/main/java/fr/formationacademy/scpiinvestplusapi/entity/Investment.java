package fr.formationacademy.scpiinvestplusapi.entity;

import java.math.BigDecimal;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private PropertyType typeProperty;
    private Integer numberShares;
    private Integer numberYears;
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "scpi_id", nullable = false)
    private Scpi scpi;

    @ManyToOne
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

}
