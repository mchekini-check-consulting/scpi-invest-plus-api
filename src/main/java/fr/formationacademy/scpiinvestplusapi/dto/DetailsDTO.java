package fr.formationacademy.scpiinvestplusapi.dto;

import java.io.Serializable;

import lombok.Getter;
/**
 * Class for converting the data gotten from the database for Scpi detail's
 */
@Getter
public class DetailsDTO implements Serializable{
    private String scpi_name;
    private Float subscription_fees;
    private Float management_costs;
    private Float share_price;
    private Float reconstitution_value;
    private Integer enjoyment_delay;
    private Boolean scheduled_payment;
    private String frequency_payment;
    private Float distribution_rate;
    private Integer minimumSubscription;
    private Float cashback;
    public DetailsDTO(String scpi_name, Float subscription_fees, Float management_costs, Float share_price,
    Float reconstitution_value, Integer enjoyment_delay, Boolean scheduled_payment,String frequency_payment, Float distribution_rate, Integer minimumSubscription, Float cashback) {
        this.scpi_name = scpi_name;
        this.subscription_fees = subscription_fees;
        this.management_costs = management_costs;
        this.share_price = share_price;
        this.reconstitution_value = reconstitution_value;
        this.enjoyment_delay = enjoyment_delay;
        this.scheduled_payment = scheduled_payment;
        this.frequency_payment = frequency_payment;
        this.distribution_rate = distribution_rate;
        this.minimumSubscription = minimumSubscription;
        this.cashback = cashback;
    }
    
}
