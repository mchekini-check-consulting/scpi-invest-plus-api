package fr.formationacademy.scpiinvestplusapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScpiSearchCriteriaDto {

    private String name;
    private BigDecimal distributionRate;
    private Integer minimumSubscription;
    private Boolean subscriptionFees;
    private String frequencyPayment;
    private List<String> locations;
    private List<String> sectors;

    public boolean hasNoFilters() {
        return (name == null || name.trim().isEmpty()) &&
                distributionRate == null &&
                minimumSubscription == null &&
                subscriptionFees == null &&
                (frequencyPayment == null || frequencyPayment.trim().isEmpty()) &&
                (locations == null || locations.isEmpty()) &&
                (sectors == null || sectors.isEmpty());
    }
}
