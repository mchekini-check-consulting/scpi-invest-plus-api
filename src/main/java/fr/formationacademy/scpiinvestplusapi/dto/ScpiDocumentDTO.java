package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScpiDocumentDTO {
    private String _class;
    private String id;
    private Long scpiId;
    private String name;
    private Float distributionRate;
    private Float sharePrice;
    private Boolean scheduledPayment;
    private Float subscriptionFeesBigDecimal;
    private Float managementCosts;
    private Long capitalization;
    private Integer enjoymentDelay;
    private String frequencyPayment;
    private Integer minimumSubscription;
    private CountryDominant countryDominant;
    private SectorDominant sectorDominant;
    private List<Location> locations;
    private List<Sector> sectors;

    @Data
    public static class CountryDominant {
        private String country;
        private Float countryPercentage;
    }

    @Data
    public static class SectorDominant {
        private String name;
        private Float sectorPercentage;
    }

    @Data
    public static class Location {
        private String country;
        private Float countryPercentage;
    }

    @Data
    public static class Sector {
        private String name;
        private Float sectorPercentage;
    }
}