package fr.formationacademy.scpiinvestplusapi.dto;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SearchScpiDto {
    @Nullable
    private String searchTerm;
    @Nullable
    private List<String> locations;
    @Nullable
    private List<String> sectors;
    @Nullable
    private Double minimumSubscription;
    @Nullable
    private Boolean subscriptionFees;
    @Nullable
    private String rentalFrequency;
    @Nullable
    private Double distributionRate;
}
