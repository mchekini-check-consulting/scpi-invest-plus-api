package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
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
    private Float mashedScore;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CountryDominant {
        private String country;
        private Float countryPercentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SectorDominant {
        private String name;
        private Float sectorPercentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private String country;
        private Float countryPercentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sector {
        private String name;
        private Float sectorPercentage;
    }
}