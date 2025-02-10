package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder

public class StatYear {
    @EmbeddedId
    private StatYeraId yearStat;
    private Float distributionRate;
    private Float sharePrice;
    private Float reconstitutionValue;
    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    private Scpi scpi;
}
