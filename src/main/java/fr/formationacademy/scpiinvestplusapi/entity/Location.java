package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Scpi scpi;
}
