package fr.formationacademy.scpiinvestplusapi.dto;

import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriteriaPoperties {
    private String ScoringType;
    private FieldValueFactorModifier modifier;
    private Double factor;
    private Double scale;
    private Double decay;
    private Double weight;
    private Long limit;

}
