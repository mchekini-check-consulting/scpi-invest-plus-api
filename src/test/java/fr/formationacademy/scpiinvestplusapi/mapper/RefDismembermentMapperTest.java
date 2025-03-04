package fr.formationacademy.scpiinvestplusapi.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RefDismembermentMapperTest {

    private final RefDismembermentMapper mapper = Mappers.getMapper(RefDismembermentMapper.class);

    @Test
    void shouldMapEntityToDto() {

        RefDismemberment entity = new RefDismemberment();
        entity.setId(1);
        entity.setYearDismemberment(10);
        entity.setRateDismemberment(new BigDecimal("4.25"));

        RefDismembermentDto dto = mapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getYearDismemberment()).isEqualTo(10);
        assertThat(dto.getRateDismemberment()).isEqualTo(new BigDecimal("4.25"));
    }

    @Test
    void shouldMapListOfEntitiesToDtoList() {
        RefDismemberment entity1 = new RefDismemberment();
        entity1.setId(3);
        entity1.setYearDismemberment(8);
        entity1.setRateDismemberment(new BigDecimal("3.5"));

        RefDismemberment entity2 = new RefDismemberment();
        entity2.setId(4);
        entity2.setYearDismemberment(12);
        entity2.setRateDismemberment(new BigDecimal("6.0"));

        List<RefDismemberment> entityList = List.of(entity1, entity2);

        List<RefDismembermentDto> dtoList = mapper.toDTOList(entityList);

        assertThat(dtoList).isNotNull();
        assertThat(dtoList).hasSize(2);

        assertThat(dtoList.get(0).getId()).isEqualTo(3);
        assertThat(dtoList.get(0).getYearDismemberment()).isEqualTo(8);
        assertThat(dtoList.get(0).getRateDismemberment()).isEqualTo(new BigDecimal("3.5"));

        assertThat(dtoList.get(1).getId()).isEqualTo(4);
        assertThat(dtoList.get(1).getYearDismemberment()).isEqualTo(12);
        assertThat(dtoList.get(1).getRateDismemberment()).isEqualTo(new BigDecimal("6.0"));
    }

}
