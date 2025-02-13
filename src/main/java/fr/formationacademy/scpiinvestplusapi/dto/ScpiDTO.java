package fr.formationacademy.scpiinvestplusapi.dto;

import java.io.Serializable;

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
    private String scpi_name;
    private Float subscription_fees;
    private Float management_costs;
    private String manager;
    private Long capitalization;
    private String iban;
    private String bic;
    private Float share_price;
    private Float reconstitution_value;
    private Integer enjoyment_delay;
    private Boolean scheduled_payment;
    private String frequency_payment;
    private Float distribution_rate;
    private Integer minimumSubscription;
    private Float cashback;
    private String advertising;



}
