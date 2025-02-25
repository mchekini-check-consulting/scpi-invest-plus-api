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
        Scpi scpi = Scpi.builder()
                .id(1)
                .name("SCPI Alpha")
                .minimumSubscription(5000)
                .manager("Manager A")
                .capitalization(1_000_000_000L)
                .subscriptionFees(BigDecimal.valueOf(2.5))
                .managementCosts(BigDecimal.valueOf(1.5))
                .enjoymentDelay(3)
                .iban("FR7630001007941234567890185")
                .bic("BICCODEXXX")
                .scheduledPayment(true)
                .cashback(1.20f)
                .advertising("Advertising A")
                .locations(List.of(
                        Location.builder()
                                .id(new LocationId(1, "France"))
                                .countryPercentage(BigDecimal.valueOf(45.5))
                                .scpi(null)
                                .build(),
                        Location.builder()
                                .id(new LocationId(1, "Germany"))
                                .countryPercentage(BigDecimal.valueOf(30.0))
                                .scpi(null)
                                .build()
                ))
                .sectors(List.of(
                        Sector.builder()
                                .id(new SectorId(1,"Real Estate" ))
                                .sectorPercentage(BigDecimal.valueOf(50))
                                .scpi(null)
                                .build(),
                        Sector.builder()
                                .id(new SectorId(1,"Technology"))
                                .sectorPercentage(BigDecimal.valueOf(25.0))
                                .scpi(null)
                                .build()
                ))
                .statYears(List.of(
                        StatYear.builder()
                                .yearStat(new StatYearId(2023, 1))
                                .distributionRate(BigDecimal.valueOf(4.5))
                                .sharePrice(BigDecimal.valueOf(200.0))
                                .reconstitutionValue(BigDecimal.valueOf(200.0))
                                .scpi(null)
                                .build(),
                        StatYear.builder()
                                .yearStat(new StatYearId(2022, 1))
                                .distributionRate(BigDecimal.valueOf(4.3))
                                .sharePrice(BigDecimal.valueOf(195.0))
                                .reconstitutionValue(BigDecimal.valueOf(250.0))
                                .scpi(null)
                                .build()
                ))
                .build();
        ScpiDtoOut dto = mapper.scpiToScpiDtoOut(scpi);

        assertNotNull(dto);
        assertNotNull(dto.getStatYear());
        assertEquals(BigDecimal.valueOf(4.5), dto.getStatYear().getDistributionRate());
        assertNotNull(dto.getLocation());
        assertEquals(1, dto.getLocation().getId().getScpiId());
        assertEquals("France", dto.getLocation().getId().getCountry());
        assertEquals(BigDecimal.valueOf(45.5), dto.getLocation().getCountryPercentage());
        assertNotNull(dto.getSector());
        assertEquals(1, dto.getSector().getId().getScpiId());
        assertEquals("Real Estate", dto.getSector().getId().getName());
        assertEquals(BigDecimal.valueOf(50), dto.getSector().getSectorPercentage());
    }
}
