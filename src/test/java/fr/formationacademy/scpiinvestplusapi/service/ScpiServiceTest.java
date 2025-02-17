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
                        .subscriptionFees(2.5f)
                        .managementCosts(1.5f)
                        .enjoymentDelay(3)
                        .iban("FR761234567890")
                        .bic("ABCDEFXX")
                        .scheduledPayment(true)
                        .frequencyPayment("Mensuelle")
                        .cashback(1.5f)
                        .advertising("High return, diversified portfolio.")
                        .locations(List.of(
                                Location.builder().id(new LocationId(1, "France")).countryPercentage(35.5f).scpi(null).build(),
                                Location.builder().id(new LocationId(1, "Portugal")).countryPercentage(5.5f).scpi(null).build(),
                                Location.builder().id(new LocationId(1, "Greece")).countryPercentage(49.0f).scpi(null).build()
                        ))
                        .sectors(List.of(
                                Sector.builder().id(new SectorId(1, "Commercial")).sectorPercentage(30.0f).scpi(null).build(),
                                Sector.builder().id(new SectorId(1, "Hotels")).sectorPercentage(20.0f).scpi(null).build(),
                                Sector.builder().id(new SectorId(1, "Ecoles")).sectorPercentage(40.0f).scpi(null).build(),
                                Sector.builder().id(new SectorId(1, "Stade")).sectorPercentage(10.0f).scpi(null).build()
                        ))
                        .statYears(List.of(
                                StatYear.builder().yearStat(new StatYearId(2021, 1)).distributionRate(4.0f).sharePrice(110.0f).reconstitutionValue(230_000_000.0f).scpi(null).build(),
                                StatYear.builder().yearStat(new StatYearId(2022, 1)).distributionRate(4.2f).sharePrice(115.0f).reconstitutionValue(240_000_000.0f).scpi(null).build()
                        ))
                        .build()
        );

        List<ScpiDtoOut> mockScpiDtoEntities = List.of(
                ScpiDtoOut.builder()
                        .id(1)
                        .name("InvestPlus")
                        .location(LocationDtoOut.builder().id(new LocationId(1, "France")).countryPercentage(35.5f).build())
                        .sector(SectorDtoOut.builder().id(new SectorId(1, "Commercial")).sectorPercentage(30.0f).build())
                        .statYear(StatYearDtoOut.builder().distributionRate(4.0f).build())
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

    @Test
    void getScpiDetailsById_ShouldReturnScpiDTO_WhenScpiExists() {
        Scpi scpiEntity = Scpi.builder()
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
