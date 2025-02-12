package fr.formationacademy.scpiinvestplusapi.model.entiry;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatYear {

    @EmbeddedId
    private StatYearKey id;

    private Float distributionRate;
    private Float sharePrice;
    private Float reconstitutionValue;

    @ManyToOne
    @MapsId("scpiId")
    @JoinColumn(name = "scpi_id", nullable = false)
    private Scpi scpi;
}


