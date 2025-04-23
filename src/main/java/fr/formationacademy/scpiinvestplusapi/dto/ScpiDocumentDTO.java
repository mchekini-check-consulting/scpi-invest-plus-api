package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private Double matchedScore;
    private Integer minimumSubscription;
    private CountryDominant countryDominant;
    private SectorDominant sectorDominant;
    private List<Location> locations;
    private List<Sector> sectors;

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

    public ScpiDocumentDTO cloneScpi(ScpiDocumentDTO scpi) {
        return ScpiDocumentDTO.builder()
                ._class(scpi.get_class())
                .id(scpi.getId())
                .scpiId(scpi.getScpiId())
                .name(scpi.getName())
                .distributionRate(scpi.getDistributionRate())
                .sharePrice(scpi.getSharePrice())
                .scheduledPayment(scpi.getScheduledPayment())
                .subscriptionFeesBigDecimal(scpi.getSubscriptionFeesBigDecimal())
                .managementCosts(scpi.getManagementCosts())
                .capitalization(scpi.getCapitalization())
                .enjoymentDelay(scpi.getEnjoymentDelay())
                .frequencyPayment(scpi.getFrequencyPayment())
                .matchedScore(scpi.getMatchedScore())
                .minimumSubscription(scpi.getMinimumSubscription())
                .countryDominant(scpi.getCountryDominant() != null
                        ? new ScpiDocumentDTO.CountryDominant(scpi.getCountryDominant().getCountry(),
                                scpi.getCountryDominant().getCountryPercentage())
                        : null)
                .sectorDominant(scpi.getSectorDominant() != null
                        ? new ScpiDocumentDTO.SectorDominant(scpi.getSectorDominant().getName(),
                                scpi.getSectorDominant().getSectorPercentage())
                        : null)
                .locations(scpi.getLocations() != null
                        ? scpi.getLocations().stream()
                                .map(loc -> new ScpiDocumentDTO.Location(loc.getCountry(), loc.getCountryPercentage()))
                                .toList()
                        : null)
                .sectors(scpi.getSectors() != null
                        ? scpi.getSectors().stream()
                                .map(sec -> new ScpiDocumentDTO.Sector(sec.getName(), sec.getSectorPercentage()))
                                .toList()
                        : null)
                .build();
    }

}