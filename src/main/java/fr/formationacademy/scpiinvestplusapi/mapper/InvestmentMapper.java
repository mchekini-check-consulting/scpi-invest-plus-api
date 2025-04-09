package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.InvestmentOutDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestmentMapper {

    @Mapping(source = "scpi.id", target = "scpiId")
    InvestmentDto toDTO(Investment investment);

    @Mapping(source = "scpi.id", target = "scpiId")
    @Mapping(target = "scpiName", source = "scpi.name")
    InvestmentOutDto toOutDto(Investment investment);

    @Mapping(source = "scpiId", target = "scpi.id")
    Investment toEntity(InvestmentDto investmentDTO);

    List<InvestmentDto> toDTOList(List<Investment> investments);

    @Mapping(target = "scpiName", source = "scpi.name")
    InvestmentDtoOut toDtoOut(Investment investment);

    List<InvestmentDtoOut> toDtoOutList(List<Investment> investmentsByInvestorEmail);

    @Mapping(source = "id", target = "id")
    default Page<InvestmentDtoOut> toDtoOutPage(Page<Investment> investments) {
        return investments.map(this::toDtoOut);
    }

}