package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDate simulationDate;
    private String investorEmail;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ScpiSimulation> scpiSimulations;

}
