package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScpiDTO implements Serializable {

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
    private List<StatYearDTO> statYears;
    private List<LocationDTO> locations;
    private List<SectorDTO> sectors;


}
