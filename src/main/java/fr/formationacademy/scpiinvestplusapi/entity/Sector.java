package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    @JoinColumn(name = "scpi_id")
    private Scpi scpi;
}
