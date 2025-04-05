package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScpiIndexDto {
    @Id
    private String id;
    private String name;
    private float distributionRate;
    private Boolean subscriptionFees;
    private BigDecimal subscriptionFeesBigDecimal;
    private Long capitalization;
    private Integer enjoymentDelay;
    private BigDecimal managementCosts;
    private String frequencyPayment;
    private List<LocationIndexDto> locations;
    private List<SectorIndexDto> sectors;
    private LocationIndexDto countryDominant;

    private SectorIndexDto sectorDominant;

    private float minimumSubscription;

}
