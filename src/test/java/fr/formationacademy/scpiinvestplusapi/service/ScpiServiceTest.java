package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.LocationDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.SectorDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearDtoOut;
import fr.formationacademy.scpiinvestplusapi.entity.*;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScpiServiceTest {
    @Mock
    private ScpiRepository scpiRepository;
    @Mock
    private ScpiMapper scpiMapper;
    @InjectMocks
    private ScpiService underTest;

    @Test
    void itShouldReturnMappedScpiDtos() {
        // Given
        List<Scpi> mockScpiEntities = List.of(
                Scpi.builder()
                        .id(1)
                        .name("SCPI Alpha")
                        .minimumSubscription(5000)
                        .manager("Manager A")
                        .capitalization(1_000_000_000L)
                        .subscriptionFees(2.5f)
                        .managementCosts(1.2f)
                        .enjoymentDelay(3)
                        .iban("FR7630001007941234567890185")
                        .bic("BICCODEXXX")
                        .scheduledPayment(true)
                        .cashback(1.20f)
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
                                        .id(new SectorId(1, "Real Estate"))
                                        .sectorPercentage(50.0f)
                                        .scpi(null)
                                        .build(),
                                Sector.builder()
                                        .id(new SectorId(1, "Technology"))
                                        .sectorPercentage(25.0f)
                                        .scpi(null)
                                        .build()
                        ))
                        .statYears(List.of(
                                StatYear.builder()
                                        .yearStat(new StatYeraId(2023, 1))
                                        .distributionRate(4.5f)
                                        .sharePrice(200.0f)
                                        .reconstitutionValue(210.0f)
                                        .scpi(null)
                                        .build(),
                                StatYear.builder()
                                        .yearStat(new StatYeraId(2022, 1))
                                        .distributionRate(4.3f)
                                        .sharePrice(195.0f)
                                        .reconstitutionValue(205.0f)
                                        .scpi(null)
                                        .build()
                        ))
                        .build()
        );

        List<ScpiDtoOut> mockScpiDtoEntities = List.of(
                ScpiDtoOut.builder()
                        .id(1)
                        .name("SCPI Alpha")
                        .location(LocationDtoOut.builder()
                                .id(new LocationId(1, "France"))
                                .countryPercentage(45.5f)
                                .build())
                        .sector(SectorDtoOut.builder()
                                .id(new SectorId(1, "Real Estate"))
                                .sectorPercentage(50.0f)
                                .build())
                        .statYear(StatYearDtoOut.builder()
                                .distributionRate(4.5f)
                                .build())
                        .build()
        );

        lenient().when(scpiRepository.findAll()).thenReturn(mockScpiEntities);
        lenient().when(scpiMapper.scpiToScpiDtoOut(mockScpiEntities)).thenReturn(mockScpiDtoEntities);

        // When
        List<ScpiDtoOut> result = underTest.getScpis();

        // Then
        verify(scpiRepository).findAll();
        verify(scpiMapper).scpiToScpiDtoOut(mockScpiEntities);
        assertNotNull(result);
        assertEquals(mockScpiDtoEntities, result);
    }
}