package fr.formationacademy.scpiinvestplusapi.entity;


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
    private BigDecimal subscriptionFees;
    private BigDecimal managementCosts;
    private String frequencyPayment;
    private List<String> locations;
    private List<String> sectors;
    private Float minimumInvestmentAmount;

}
