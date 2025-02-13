package fr.formationacademy.scpiinvestplusapi.model.entiry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectorKey implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "scpi_id")
    private Integer scpiId;
}
