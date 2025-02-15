package fr.formationacademy.scpiinvestplusapi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "scpi", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Scpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer minimumSubscription;
    private String manager;
    private BigDecimal capitalization;
    private Float subscriptionFees;
    private Float managementCosts;
    private Integer enjoymentDelay;

    @Column(unique = true)
    private String iban;

    private String bic;

    private Boolean scheduledPayment;
    private BigDecimal cashback;
    private String advertising;

    @OneToMany(mappedBy = "scpi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatYear> statYears = new ArrayList<>();

    @OneToMany(mappedBy = "scpi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sector> sectors = new ArrayList<>();

    @OneToMany(mappedBy = "scpi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations = new ArrayList<>();
}
