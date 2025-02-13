package fr.formationacademy.scpiinvestplusapi.mappers;

import fr.formationacademy.scpiinvestplusapi.model.dto.requests.InvestorRequest;
import fr.formationacademy.scpiinvestplusapi.model.dto.requests.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.model.entiry.Investor;
import fr.formationacademy.scpiinvestplusapi.model.entiry.Scpi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(target = "email", ignore = true)
    Investor updateInvestor(@MappingTarget Investor existing, Investor updated);

    @Mapping(target = "id", ignore = true)
    Scpi updateScpi(@MappingTarget Scpi existing, Scpi updated);

    Investor toInvestor(InvestorRequest investorDto);
    Scpi toScpi(ScpiDto scpiDto);
}

