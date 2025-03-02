package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {
    @Id
    private Integer id;
    private String functionality;
    private Boolean standard;
    private Boolean premium;
    private String description;
}
