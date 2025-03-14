package fr.formationacademy.scpiinvestplusapi.entity;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
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
    private PropertyType propertyType;

    @ManyToOne
    @MapsId("simulationId")
    @JoinColumn(name = "simulation_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Simulation simulation;

    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    @ToString.Exclude
    private Scpi scpi;
}
