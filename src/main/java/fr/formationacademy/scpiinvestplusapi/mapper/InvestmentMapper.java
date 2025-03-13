package fr.formationacademy.scpiinvestplusapi.mapper;

import java.util.List;

import fr.formationacademy.scpiinvestplusapi.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.formationacademy.scpiinvestplusapi.entity.Investment;

@Mapper(componentModel = "spring")
public interface InvestmentMapper {

    @Mapping(source = "scpi.id", target = "scpiId")
    InvestmentDto toDTO(Investment investor);

    @Mapping(source = "scpiId", target = "scpi.id")
    Investment toEntity(InvestmentDto investmentDTO);

    List<InvestmentDto> toDTOList(List<Investment> investments);

    @Mapping(target = "scpiName", source = "scpi.name")
    InvestmentDtoOut toDtoOut(Investment investment);

    List<InvestmentDtoOut> toDtoOutList(List<Investment> investmentsByInvestorEmail);

}