package fr.formationacademy.scpiinvestplusapi.dto;

import java.io.Serializable;
import java.util.List;

import lombok.*;

/**
 * Class for converting the data gotten from the database for Scpi detail's
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScpiDTO implements Serializable{

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




}
