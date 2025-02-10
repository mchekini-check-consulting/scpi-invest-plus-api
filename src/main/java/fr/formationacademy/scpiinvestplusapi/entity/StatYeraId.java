package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatYeraId implements java.io.Serializable {
    private Integer yearStat;
    private Long scpiId;
}
