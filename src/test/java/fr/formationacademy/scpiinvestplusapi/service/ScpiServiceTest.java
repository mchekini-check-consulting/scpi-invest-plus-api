package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.*;
import fr.formationacademy.scpiinvestplusapi.entity.*;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
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
                        .name("InvestPlus")
                        .minimumSubscription(1000)
                        .manager("SCPI Gestion")
                        .capitalization(100_000_000L)
                        .subscriptionFees(BigDecimal.valueOf(2.5))
                        .managementCosts(BigDecimal.valueOf(2.5))
                        .enjoymentDelay(3)
                        .iban("FR761234567890")
                        .bic("ABCDEFXX")
                        .scheduledPayment(true)
                        .frequencyPayment("Mensuelle")
                        .cashback(1.5f)
                        .advertising("High return, diversified portfolio.")
                        .locations(List.of(
                                Location.builder().id(new LocationId(1, "France")).countryPercentage(new BigDecimal("35.5")).scpi(null).build(),
                                Location.builder().id(new LocationId(1, "Portugal")).countryPercentage(new BigDecimal("5.5")).scpi(null).build(),
                                Location.builder().id(new LocationId(1, "Greece")).countryPercentage(new BigDecimal("49.0")).scpi(null).build()

                        ))
                        .sectors(List.of(
                                Sector.builder().id(new SectorId(1, "Commercial")).sectorPercentage(new BigDecimal("30.0")).scpi(null).build(),
                                Sector.builder().id(new SectorId(1, "Hotels")).sectorPercentage(new BigDecimal("20.0")).scpi(null).build(),
                                Sector.builder().id(new SectorId(1, "Ecoles")).sectorPercentage(new BigDecimal("40.0")).scpi(null).build(),
                                Sector.builder().id(new SectorId(1, "Stade")).sectorPercentage(new BigDecimal("10.0")).scpi(null).build()
                        ))
                        .statYears(List.of(
                                StatYear.builder().yearStat(new StatYearId(2021, 1))
                                        .distributionRate(new BigDecimal("4.0"))
                                        .sharePrice(new BigDecimal("110.0"))
                                        .reconstitutionValue(new BigDecimal("230000000.0"))
                                        .scpi(null).build(),
                                StatYear.builder().yearStat(new StatYearId(2022, 1))
                                        .distributionRate(new BigDecimal("4.2"))
                                        .sharePrice(new BigDecimal("115.0"))
                                        .reconstitutionValue(new BigDecimal("240000000.0"))
                                        .scpi(null).build()
                        ))

                        .build()
        );

        List<ScpiDtoOut> mockScpiDtoEntities = List.of(
                ScpiDtoOut.builder()
                        .id(1)
                        .name("InvestPlus")
                        .location(LocationDtoOut.builder().id(new LocationId(1, "France")).countryPercentage(BigDecimal.valueOf(4.5)).build())
                        .sector(SectorDtoOut.builder().id(new SectorId(1, "Commercial")).sectorPercentage(BigDecimal.valueOf(30.0)).build())
                        .statYear(StatYearDtoOut.builder().distributionRate(BigDecimal.valueOf(4.0)).build())
                        .build()
        );

        lenient().when(scpiRepository.findAll()).thenReturn(mockScpiEntities);
        lenient().when(scpiMapper.scpiToScpiDtoOut(mockScpiEntities)).thenReturn(mockScpiDtoEntities);
        List<ScpiDtoOut> result = underTest.getScpis();
        verify(scpiRepository).findAll();
        verify(scpiMapper).scpiToScpiDtoOut(mockScpiEntities);
        assertNotNull(result);
        assertEquals(mockScpiDtoEntities, result);
    }

    @Test
    void getScpiDetailsById_ShouldReturnScpiDTO_WhenScpiExists() {
        Scpi scpiEntity = Scpi.builder()
                .id(1)
                .name("InvestPlus")
                .minimumSubscription(1000)
                .manager("SCPI Gestion")
                .capitalization(100000000L)
                .subscriptionFees(BigDecimal.valueOf(2.5))
                .managementCosts(BigDecimal.valueOf(1.5))
                .enjoymentDelay(3)
                .iban("FR761234567890")
                .bic("ABCDEFXX")
                .scheduledPayment(true)
                .frequencyPayment("Mensuelle")
                .cashback(1.5f)
                .advertising("High return, diversified portfolio.")
                .build();

        ScpiDtoOut expectedScpiDTO = ScpiDtoOut.builder()
                .id(1)
                .name("InvestPlus")
                .minimumSubscription(1000)
                .manager("SCPI Gestion")
                .capitalization(100000000L)
                .subscriptionFees(2.5f)
                .managementCosts(1.5f)
                .enjoymentDelay(3)
                .iban("FR761234567890")
                .bic("ABCDEFXX")
                .scheduledPayment(true)
                .frequencyPayment("Mensuelle")
                .cashback(1.5f)
                .advertising("High return, diversified portfolio.")
                .build();

        when(scpiRepository.findById(1)).thenReturn(Optional.of(scpiEntity));
        when(scpiMapper.scpiToScpiDtoOut(scpiEntity)).thenReturn(expectedScpiDTO);

        ScpiDtoOut result = underTest.getScpiDetailsById(1);

        assertNotNull(result);
        assertEquals(expectedScpiDTO.getId(), result.getId());
        assertEquals(expectedScpiDTO.getName(), result.getName());
        assertEquals(expectedScpiDTO.getManager(), result.getManager());
        assertEquals(expectedScpiDTO.getSubscriptionFees(), result.getSubscriptionFees());
    }

    @Test
    void getScpiDetailsById_ShouldReturnNull_WhenScpiDoesNotExist() {
        // Arrange
        when(scpiRepository.findById(2)).thenReturn(Optional.empty());

        // Act
        ScpiDtoOut result = underTest.getScpiDetailsById(2);

        // Assert
        assertNull(result);

        // Verify interactions
        verify(scpiRepository, times(1)).findById(2);
    }
}