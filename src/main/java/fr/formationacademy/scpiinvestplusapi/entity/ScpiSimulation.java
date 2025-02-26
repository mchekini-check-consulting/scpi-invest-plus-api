package fr.formationacademy.scpiinvestplusapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ScpiSimulation {

    @EmbeddedId
    private ScpiSimulationId scpiSimulationId;
    private Integer numberPart;
    private BigDecimal partPrice;
    private BigDecimal rising;
    private Integer duree;
    private BigDecimal dureePercentage;
    private String propertyType;

    @ManyToOne
    @MapsId("simulationId")
    @JoinColumn(name = "simulation_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Simulation simulation;
    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Scpi scpi;
}
