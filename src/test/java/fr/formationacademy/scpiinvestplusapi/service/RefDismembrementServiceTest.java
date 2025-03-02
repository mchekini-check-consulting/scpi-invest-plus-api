
/*
package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;
import fr.formationacademy.scpiinvestplusapi.mapper.RefDismembermentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.RefDismembermentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefDismembermentServiceTest {

    @Mock
    private RefDismembermentRepository repository;

    @Mock
    private RefDismembermentMapper mapper;

    @InjectMocks
    private RefDismembermentService service;

    private RefDismemberment entity;
    private RefDismembermentDto dto;

    @BeforeEach
    void setUp() {
        entity = new RefDismemberment();
        entity.setId(1);
        entity.setPropertyType("Nue-propriétaire");
        entity.setYearDismemberment(2024);
        entity.setRateDismemberment(BigDecimal.valueOf(3.5));

        dto = new RefDismembermentDto();
        dto.setId(1);
        dto.setYearDismemberment(2024);
        dto.setRateDismemberment(BigDecimal.valueOf(3.5));
    }

    @Test
    void shouldReturnRefDismembermentByPropertyType() {
        // GIVEN
        when(repository.findByPropertyType("Nue-propriétaire")).thenReturn(List.of(entity));
        when(mapper.toDTOList(List.of(entity))).thenReturn(List.of(dto));

        // WHEN
        List<RefDismembermentDto> result = service.getByPropertyType("Nue-propriétaire");

        // THEN
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getYearDismemberment()).isEqualTo(2024);
        assertThat(result.get(0).getRateDismemberment()).isEqualTo(BigDecimal.valueOf(3.5));

        verify(repository).findByPropertyType("Nue-propriétaire");
        verify(mapper).toDTOList(List.of(entity));
    }
}
*/