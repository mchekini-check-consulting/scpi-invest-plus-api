package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Sector {
    @EmbeddedId
    private SectorId id;
    private Float sectorPercentage;

    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    @ToString.Exclude
    private Scpi scpi;
}
