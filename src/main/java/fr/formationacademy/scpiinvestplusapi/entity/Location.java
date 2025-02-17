package fr.formationacademy.scpiinvestplusapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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
    @JsonIgnore
    private Scpi scpi;
}
