package fr.formationacademy.scpiinvestplusapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Scpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer minimumSubscription;
    private String manager;
    private Long capitalization;
    private Float subscriptionFees;
    private Float managementCosts;
    private Integer enjoymentDelay;
    private String iban;
    private String bic;
    private Boolean scheduledPayment;
    private String frequencyPayment;
    private Float cashback;
    private String advertising;

    @OneToMany(mappedBy = "scpi",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Location> locations;

    @OneToMany(mappedBy = "scpi",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Sector> sectors;

    @OneToMany(mappedBy = "scpi",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<StatYear> statYears;
}
