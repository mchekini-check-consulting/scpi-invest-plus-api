package fr.formationacademy.scpiinvestplusapi.batch.processor;

import fr.formationacademy.scpiinvestplusapi.model.dto.BatchDataDto;
import fr.formationacademy.scpiinvestplusapi.model.dto.requests.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.model.entiry.Scpi;
import fr.formationacademy.scpiinvestplusapi.repositories.ScpiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScpiItemProcessorTest {

    @Mock
    private ScpiRepository scpiRepository;

    @InjectMocks
    private ScpiItemProcessor scpiItemProcessor;

    @BeforeEach
    void setUp() {
        when(scpiRepository.findAll()).thenReturn(Collections.emptyList());
        scpiItemProcessor.init();
    }

    @Test
    void testProcess_NewScpi() {
        ScpiDto dto = ScpiDto.builder()
                .id(1)
                .name("Test SCPI")
                .minimumSubscription(1000)
                .manager("Test Manager")
                .capitalization(BigDecimal.valueOf(50000.0))
                .subscriptionFees(2.5f)
                .managementCosts(1.5f)
                .enjoymentDelay(30)
                .iban("FR7630004000031234567890143")
                .bic("BNPAFRPPXXX")
                .scheduledPayment(true)
                .cashback(BigDecimal.valueOf(100.0))
                .advertising("Special Offer")
                .build();

        BatchDataDto batchDataDto = new BatchDataDto();
        batchDataDto.setScpiDto(dto);

        Scpi result = scpiItemProcessor.process(batchDataDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test SCPI", result.getName());
        assertEquals(1000, result.getMinimumSubscription());
        assertEquals("Test Manager", result.getManager());
        assertEquals(0, result.getCapitalization().compareTo(BigDecimal.valueOf(50000.0)));
        assertEquals(2.5f, result.getSubscriptionFees());
        assertEquals(1.5f, result.getManagementCosts());
        assertEquals(30, result.getEnjoymentDelay());
        assertEquals("FR7630004000031234567890143", result.getIban());
        assertEquals("BNPAFRPPXXX", result.getBic());
        assertTrue(result.getScheduledPayment());
        assertEquals(0, result.getCashback().compareTo(BigDecimal.valueOf(100.0)));
        assertEquals("Special Offer", result.getAdvertising());
    }

    @Test
    void testProcess_ExistingUnchangedScpi() {
        Scpi existingScpi = new Scpi();
        existingScpi.setId(1);
        existingScpi.setName("Test SCPI");
        existingScpi.setMinimumSubscription(1000);
        existingScpi.setManager("Test Manager");
        existingScpi.setCapitalization(BigDecimal.valueOf(50000.0));
        existingScpi.setSubscriptionFees(2.5f);
        existingScpi.setManagementCosts(1.5f);
        existingScpi.setEnjoymentDelay(30);
        existingScpi.setIban("FR7630004000031234567890143");
        existingScpi.setBic("BNPAFRPPXXX");
        existingScpi.setScheduledPayment(true);
        existingScpi.setCashback(BigDecimal.valueOf(100.0));
        existingScpi.setAdvertising("Special Offer");

        when(scpiRepository.findAll()).thenReturn(List.of(existingScpi));
        scpiItemProcessor.init();

        ScpiDto dto = ScpiDto.builder()
                .id(1)
                .name("Test SCPI")
                .minimumSubscription(1000)
                .manager("Test Manager")
                .capitalization(BigDecimal.valueOf(50000.0))
                .subscriptionFees(2.5f)
                .managementCosts(1.5f)
                .enjoymentDelay(30)
                .iban("FR7630004000031234567890143")
                .bic("BNPAFRPPXXX")
                .scheduledPayment(true)
                .cashback(BigDecimal.valueOf(100.0))
                .advertising("Special Offer")
                .build();

        BatchDataDto batchDataDto = new BatchDataDto();
        batchDataDto.setScpiDto(dto);

        Scpi result = scpiItemProcessor.process(batchDataDto);

        assertNull(result);
    }

    @Test
    void testProcess_NullScpi() {
        BatchDataDto batchDataDto = new BatchDataDto();
        batchDataDto.setScpiDto(null);

        Scpi result = scpiItemProcessor.process(batchDataDto);
        assertNull(result);
    }
}
