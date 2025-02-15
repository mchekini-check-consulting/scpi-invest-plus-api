package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ScpiMapperTest {
    private final ScpiMapper mapper = Mappers.getMapper(ScpiMapper.class);


    @Test
    void itShouldMapCorrectlyFromScpiToScpiDto() {
        // Given
        Scpi scpi = Scpi.builder()
                .id(1)
                .name("SCPI Alpha")
                .minimumSubscription(5000)
                .manager("Manager A")
                .capitalization(BigDecimal.valueOf(50000.0))
                .subscriptionFees(2.5f)
                .managementCosts(1.2f)
                .enjoymentDelay(3)
                .iban("FR7630001007941234567890185")
                .bic("BICCODEXXX")
                .scheduledPayment(true)
                .cashback(BigDecimal.valueOf(100.0))
                .advertising("Advertising A")
                .locations(List.of(
                        Location.builder()
                                .id(new LocationId(1, "France"))
                                .countryPercentage(45.5f)
                                .scpi(null)
                                .build(),
                        Location.builder()
                                .id(new LocationId(1, "Germany"))
                                .countryPercentage(30.0f)
                                .scpi(null)
                                .build()
                ))
                .sectors(List.of(
                        Sector.builder()
                                .id(new SectorId("Real Estate",1 ))
                                .sectorPercentage(50.0f)
                                .scpi(null)
                                .build(),
                        Sector.builder()
                                .id(new SectorId("Technology",1))
                                .sectorPercentage(25.0f)
                                .scpi(null)
                                .build()
                ))
                .statYears(List.of(
                        StatYear.builder()
                                .yearStat(new StatYearId(2023, 1))
                                .distributionRate(4.5f)
                                .sharePrice(200.0f)
                                .reconstitutionValue(210.0f)
                                .scpi(null)
                                .build(),
                        StatYear.builder()
                                .yearStat(new StatYearId(2022, 1))
                                .distributionRate(4.3f)
                                .sharePrice(195.0f)
                                .reconstitutionValue(205.0f)
                                .scpi(null)
                                .build()
                ))
                .build();


        // When
        ScpiDtoOut dto = mapper.scpiToScpiDtoOut(scpi);

        assertNotNull(dto);

        // Validate StatYear Mapping
        assertNotNull(dto.getStatYear());
        assertEquals(4.5f, dto.getStatYear().getDistributionRate());

        // Validate Location Mapping (Highest Percentage)
        assertNotNull(dto.getLocation());
        assertEquals(1, dto.getLocation().getId().getScpiId());
        assertEquals("France", dto.getLocation().getId().getCountry());
        assertEquals(45.5f, dto.getLocation().getCountryPercentage());

        // Validate Sector Mapping (Highest Percentage)
        assertNotNull(dto.getSector());
        assertEquals(1, dto.getSector().getId().getScpiId());
        assertEquals("Real Estate", dto.getSector().getId().getName());
        assertEquals(50.0f, dto.getSector().getSectorPercentage());
    }
}
