package fr.formationacademy.scpiinvestplusapi.model.entiry;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @EmbeddedId
    private LocationKey id;

    private Float countryPercentage;

    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    private Scpi scpi;

}


