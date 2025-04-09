package fr.formationacademy.scpiinvestplusapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RefDismembermentMapper {

    RefDismembermentDto toDTO(RefDismemberment refDismemberment);

    List<RefDismembermentDto> toDTOList(List<RefDismemberment> refDismemberments);

    RefDismemberment toEntity(RefDismembermentDto refDismembermentDTO);

    List<RefDismemberment> toEntityList(List<RefDismembermentDto> refDismembermentDTOs);

}
