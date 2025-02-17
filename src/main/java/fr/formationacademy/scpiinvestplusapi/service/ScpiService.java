package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.SearchScpiDto;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScpiService {
    private final ScpiRepository scpiRepository;
    private final ScpiMapper scpiMapper;

    public ScpiService(ScpiRepository scpiRepository, ScpiMapper scpiMapper) {
        this.scpiRepository = scpiRepository;
        this.scpiMapper = scpiMapper;
    }

    public List<ScpiDtoOut> getScpis() {
        List<Scpi> scpis = scpiRepository.findAll();
        return scpiMapper.scpiToScpiDtoOut(scpis);
    }
    public List<ScpiDtoOut> getScpiWithFilter(SearchScpiDto searchScpiDto) {
        double minimumSubscription = (searchScpiDto.getMinimumSubscription() != null)
                ? searchScpiDto.getMinimumSubscription() : 0.0;
        double distributionRate = (searchScpiDto.getDistributionRate() != null)
                ? searchScpiDto.getDistributionRate() : 0.0;
        Boolean subscriptionFees = (searchScpiDto.getSubscriptionFees() != null)
                ? searchScpiDto.getSubscriptionFees() : false;

        List<Scpi> scpis = scpiRepository.searchScpi(
                searchScpiDto.getSearchTerm(),
                searchScpiDto.getLocations(),
                searchScpiDto.getSectors(),
                minimumSubscription,
                distributionRate,
                subscriptionFees
        );
        return scpis.stream()
                .map(scpiMapper::scpiToScpiDtoOut)
                .collect(Collectors.toList());
    }
}
