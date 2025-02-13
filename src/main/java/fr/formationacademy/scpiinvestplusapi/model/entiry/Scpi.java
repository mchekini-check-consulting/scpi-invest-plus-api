package fr.formationacademy.scpiinvestplusapi.model.entiry;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(unique = true)
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
