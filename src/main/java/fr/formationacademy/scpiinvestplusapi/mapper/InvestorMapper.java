package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.dto.KeycloakUserDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvestorMapper {
    InvestorDTO toDTO(Investor investor);

    Investor toEntity(InvestorDTO investorDTO);

    Investor KeycloakUserToInvestor(KeycloakUserDto keycloakUserDto);
}