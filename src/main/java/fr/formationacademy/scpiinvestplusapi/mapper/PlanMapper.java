package fr.formationacademy.scpiinvestplusapi.mapper;

import fr.formationacademy.scpiinvestplusapi.dto.PlanDto;
import fr.formationacademy.scpiinvestplusapi.entity.Plan;
import org.mapstruct.*;

@Mapper (componentModel = "spring")
public interface PlanMapper {

    PlanDto toPlanDto(Plan plan);
    Plan toPlan(PlanDto planDto);

}
