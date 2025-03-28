package fr.formationacademy.scpiinvestplusapi.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "scpi")
public class ScpiIndex {

    @Id
    private String id;
    private String name;
    private Integer minimumSubscription;
    private String manager;
    private Long capitalization;
    private BigDecimal subscriptionFees;
    private BigDecimal managementCosts;
    private Integer enjoymentDelay;
    private String frequencyPayment;
    private Float cashback;
    private String advertising;
    @OneToMany(mappedBy = "scpi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    @OneToMany(mappedBy = "scpi", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Sector> sectors;

}
