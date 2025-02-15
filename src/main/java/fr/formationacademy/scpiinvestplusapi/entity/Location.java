package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    @EmbeddedId
    private LocationId id;
    private Float countryPercentage;

    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id",nullable = false)
    @ToString.Exclude
    private Scpi scpi;
}
