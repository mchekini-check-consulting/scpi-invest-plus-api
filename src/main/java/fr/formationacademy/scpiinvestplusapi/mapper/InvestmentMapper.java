package fr.formationacademy.scpiinvestplusapi.mapper;

import java.util.List;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDtoOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;

@Mapper(componentModel = "spring")
public interface InvestmentMapper {

    @Mapping(source = "scpi.id", target = "scpiId")
    InvestmentDto toDTO(Investment investor);

    @Mapping(source = "scpiId", target = "scpi.id")
    @Mapping(target = "investor", ignore = true)
    Investment toEntity(InvestmentDto investmentDTO);

    List<InvestmentDto> toDTOList(List<Investment> investments);

    List<Investment> toEntityList(List<InvestmentDto> investmentDTOs);

    @Mapping(target = "scpiName", source = "scpi.name")
    InvestmentDtoOut toDtoOut(Investment investment);

    List<InvestmentDtoOut> toDtoOutList(List<Investment> investments);
}