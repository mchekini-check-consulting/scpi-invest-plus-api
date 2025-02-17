package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class StatYear {
    @EmbeddedId
    private StatYearId yearStat;
    private Float distributionRate;
    private Float sharePrice;
    private Float reconstitutionValue;

    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    @ToString.Exclude
    private Scpi scpi;
}
