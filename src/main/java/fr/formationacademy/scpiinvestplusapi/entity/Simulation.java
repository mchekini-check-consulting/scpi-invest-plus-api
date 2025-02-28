package fr.formationacademy.scpiinvestplusapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private  String name;
    private LocalDate simulationDate;

    @ManyToOne
    @JoinColumn(name = "investor_email", nullable = false)
    private Investor investor;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ScpiSimulation> scpiSimulations;

}
