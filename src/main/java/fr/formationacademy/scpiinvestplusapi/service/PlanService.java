package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.PlanDto;
import fr.formationacademy.scpiinvestplusapi.mapper.PlanMapper;
import fr.formationacademy.scpiinvestplusapi.repository.PlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    public PlanService(PlanRepository planRepository, PlanMapper planMapper) {
        this.planRepository = planRepository;
        this.planMapper = planMapper;
    }

    public List<PlanDto> getAllPlans() {
        List<PlanDto> plans = planRepository.findAll().stream().map(planMapper::toPlanDto).toList();
        log.info("Fetched all plans successfully. Total plans: {}", plans.size());
        return plans;
    }
}
