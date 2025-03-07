package fr.formationacademy.scpiinvestplusapi.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class InvestmentMapperTest {

    private final InvestmentMapper investmentMapper = Mappers.getMapper(InvestmentMapper.class);

    @Test
    void shouldMapEntityToDto() {
        Scpi scpi = new Scpi();
        scpi.setId(1);

        Investment investment = Investment.builder()
                .typeProperty(PropertyType.NUE_PROPRIETE)
                .numberShares(10000)
                .numberYears(5)
                .totalAmount(new BigDecimal("10000.50"))
                .investmentState("En cours")
                .scpi(scpi)
                .build();

        InvestmentDto dto = investmentMapper.toDTO(investment);

        assertThat(dto).isNotNull();
        assertThat(dto.getTypeProperty()).isEqualTo(PropertyType.NUE_PROPRIETE);
        assertThat(dto.getInvestmentState()).isEqualTo("En cours");
        assertThat(dto.getNumberShares()).isEqualTo(10000);
        assertThat(dto.getNumberYears()).isEqualTo(5);
        assertThat(dto.getTotalAmount()).isEqualTo(new BigDecimal("10000.50"));
        assertThat(dto.getScpiId()).isEqualTo(1);
    }

    @Test
    void shouldMapDtoToEntity() {
        InvestmentDto dto = new InvestmentDto();
        dto.setTypeProperty(PropertyType.NUE_PROPRIETE);
        dto.setNumberShares(10000);
        dto.setNumberYears(10);
        dto.setInvestmentState("En cours");
        dto.setTotalAmount(new BigDecimal("20000.75"));
        dto.setScpiId(2);

        Investment entity = investmentMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getTypeProperty()).isEqualTo(PropertyType.NUE_PROPRIETE);
        assertThat(entity.getNumberShares()).isEqualTo(10000);
        assertThat(entity.getNumberYears()).isEqualTo(10);
        assertThat(entity.getTotalAmount()).isEqualTo(new BigDecimal("20000.75"));
        assertThat(entity.getScpi()).isNotNull();
        assertThat(entity.getInvestmentState()).isEqualTo("En cours");
        assertThat(entity.getScpi().getId()).isEqualTo(2);
    }

    @Test
    void shouldMapListOfEntitiesToDtoList() {
        Scpi scpi1 = new Scpi();
        scpi1.setId(1);
        Investment investment1 = Investment.builder()
                .id(1)
                .typeProperty(PropertyType.USUFRUIT)
                .numberShares(10000)
                .numberYears(3)
                .totalAmount(new BigDecimal("5000.00"))
                .investmentState("En cours")
                .scpi(scpi1)
                .build();

        Scpi scpi2 = new Scpi();
        scpi2.setId(2);
        Investment investment2 = Investment.builder()
                .id(2)
                .typeProperty(PropertyType.NUE_PROPRIETE)
                .numberShares(10000)
                .numberYears(6)
                .totalAmount(new BigDecimal("8000.00"))
                .investmentState("En cours")
                .scpi(scpi2)
                .build();

        List<Investment> investmentList = List.of(investment1, investment2);
        List<InvestmentDto> dtoList = investmentMapper.toDTOList(investmentList);

        assertThat(dtoList).isNotNull();
        assertThat(dtoList).hasSize(2);

        assertThat(dtoList.get(0).getTypeProperty()).isEqualTo(PropertyType.USUFRUIT);
        assertThat(dtoList.get(0).getInvestmentState()).isEqualTo("En cours");
        assertThat(dtoList.get(0).getNumberShares()).isEqualTo(10000);
        assertThat(dtoList.get(0).getTotalAmount()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(dtoList.get(0).getScpiId()).isEqualTo(1);

        assertThat(dtoList.get(1).getTypeProperty()).isEqualTo(PropertyType.NUE_PROPRIETE);
        assertThat(dtoList.get(1).getInvestmentState()).isEqualTo("En cours");
        assertThat(dtoList.get(1).getNumberShares()).isEqualTo(10000);
        assertThat(dtoList.get(1).getTotalAmount()).isEqualTo(new BigDecimal("8000.00"));
        assertThat(dtoList.get(1).getScpiId()).isEqualTo(2);
    }
}