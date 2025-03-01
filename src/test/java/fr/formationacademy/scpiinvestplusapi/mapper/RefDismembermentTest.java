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
        // GIVEN
        RefDismemberment entity = new RefDismemberment();
        entity.setId(1);
        entity.setYearDismemberment(10);
        entity.setRateDismemberment(new BigDecimal("4.25"));

        // WHEN
        RefDismembermentDto dto = mapper.toDTO(entity);

        // THEN
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getYearDismemberment()).isEqualTo(10);
        assertThat(dto.getRateDismemberment()).isEqualTo(new BigDecimal("4.25"));
    }

    @Test
    void shouldMapDtoToEntityIgnoringPropertyType() {
        // GIVEN
        RefDismembermentDto dto = new RefDismembermentDto();
        dto.setId(2);
        dto.setYearDismemberment(15);
        dto.setRateDismemberment(new BigDecimal("5.75"));

        // WHEN
        RefDismemberment entity = mapper.toEntity(dto);

        // THEN
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2);
        assertThat(entity.getYearDismemberment()).isEqualTo(15);
        assertThat(entity.getRateDismemberment()).isEqualTo(new BigDecimal("5.75"));
        assertThat(entity.getPropertyType()).isNull(); // Vérifie que propertyType est bien ignoré
    }

    @Test
    void shouldMapListOfEntitiesToDtoList() {
        // GIVEN
        RefDismemberment entity1 = new RefDismemberment();
        entity1.setId(3);
        entity1.setYearDismemberment(8);
        entity1.setRateDismemberment(new BigDecimal("3.5"));

        RefDismemberment entity2 = new RefDismemberment();
        entity2.setId(4);
        entity2.setYearDismemberment(12);
        entity2.setRateDismemberment(new BigDecimal("6.0"));

        List<RefDismemberment> entityList = List.of(entity1, entity2);

        // WHEN
        List<RefDismembermentDto> dtoList = mapper.toDTOList(entityList);

        // THEN
        assertThat(dtoList).isNotNull();
        assertThat(dtoList).hasSize(2);

        assertThat(dtoList.get(0).getId()).isEqualTo(3);
        assertThat(dtoList.get(0).getYearDismemberment()).isEqualTo(8);
        assertThat(dtoList.get(0).getRateDismemberment()).isEqualTo(new BigDecimal("3.5"));

        assertThat(dtoList.get(1).getId()).isEqualTo(4);
        assertThat(dtoList.get(1).getYearDismemberment()).isEqualTo(12);
        assertThat(dtoList.get(1).getRateDismemberment()).isEqualTo(new BigDecimal("6.0"));
    }

    @Test
    void shouldMapListOfDtosToEntityListIgnoringPropertyType() {
        // GIVEN
        RefDismembermentDto dto1 = new RefDismembermentDto();
        dto1.setId(5);
        dto1.setYearDismemberment(7);
        dto1.setRateDismemberment(new BigDecimal("2.75"));

        RefDismembermentDto dto2 = new RefDismembermentDto();
        dto2.setId(6);
        dto2.setYearDismemberment(20);
        dto2.setRateDismemberment(new BigDecimal("7.25"));

        List<RefDismembermentDto> dtoList = List.of(dto1, dto2);

        // WHEN
        List<RefDismemberment> entityList = mapper.toEntityList(dtoList);

        // THEN
        assertThat(entityList).isNotNull();
        assertThat(entityList).hasSize(2);

        assertThat(entityList.get(0).getId()).isEqualTo(5);
        assertThat(entityList.get(0).getYearDismemberment()).isEqualTo(7);
        assertThat(entityList.get(0).getRateDismemberment()).isEqualTo(new BigDecimal("2.75"));
        assertThat(entityList.get(0).getPropertyType()).isNull(); // Vérification de l'ignorance

        assertThat(entityList.get(1).getId()).isEqualTo(6);
        assertThat(entityList.get(1).getYearDismemberment()).isEqualTo(20);
        assertThat(entityList.get(1).getRateDismemberment()).isEqualTo(new BigDecimal("7.25"));
        assertThat(entityList.get(1).getPropertyType()).isNull(); // Vérification de l'ignorance
    }
}
