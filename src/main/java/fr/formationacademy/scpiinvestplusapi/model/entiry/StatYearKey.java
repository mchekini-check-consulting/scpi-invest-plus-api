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
public class StatYearKey implements Serializable {

    @Column(name = "year_stat")
    private Integer yearStat;

    @Column(name = "scpi_id")
    private Integer scpiId;
}
