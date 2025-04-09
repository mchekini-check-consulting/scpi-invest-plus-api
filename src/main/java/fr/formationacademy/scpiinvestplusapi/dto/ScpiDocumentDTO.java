package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ScpiDocumentDTO {
    private String _class;
    private String id;
    private Long scpiId;
    private String name;
    private Double distributionRate;
    private Double subscriptionFeesBigDecimal;
    private Double managementCosts;
    private Long capitalization;
    private Long enjoymentDelay;
    private String frequencyPayment;
    private Long minimumSubscription;
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
        private Integer id;
        private Float countryPercentage;
    }

    @Data
    public static class Sector {
        private Float sectorPercentage;
    }
}