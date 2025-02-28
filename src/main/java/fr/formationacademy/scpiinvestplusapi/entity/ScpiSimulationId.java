package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScpiSimulationId implements Serializable {
    @Column(name="simulation_id")
    private Integer simulationId;
    @Column(name="scpi_id")
    private Integer scpiId;
}
