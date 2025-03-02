package fr.formationacademy.scpiinvestplusapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;

@Mapper(componentModel = "spring")
public interface RefDismembermentMapper {
    RefDismembermentDto toDTO(RefDismemberment refDismemberment);

    List<RefDismembermentDto> toDTOList(List<RefDismemberment> refDismemberments);

}
