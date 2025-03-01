package fr.formationacademy.scpiinvestplusapi.entity;

import java.math.BigDecimal;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefDismemberment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String propertyType;
    private Integer yearDismemberment;
    private BigDecimal rateDismemberment;
}
