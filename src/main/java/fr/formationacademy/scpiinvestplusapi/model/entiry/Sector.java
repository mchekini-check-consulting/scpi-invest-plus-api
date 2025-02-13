package fr.formationacademy.scpiinvestplusapi.model.entiry;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sector {

    @EmbeddedId
    private SectorKey id;

    private Integer sectorPercentage;

    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    private Scpi scpi;
}

