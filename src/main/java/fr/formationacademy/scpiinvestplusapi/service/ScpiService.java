package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDtoOut;
import fr.formationacademy.scpiinvestplusapi.dto.SearchScpiDto;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScpiService {
    private final ScpiRepository scpiRepository;
    private final ScpiMapper scpiMapper;

    public ScpiService(ScpiRepository scpiRepository, ScpiMapper scpiMapper) {
        this.scpiRepository = scpiRepository;
        this.scpiMapper = scpiMapper;
    }

    public List<ScpiDtoOut> getScpis() {
        List<Scpi> scpis = scpiRepository.findAllOrderByLatestDistributionRateDesc();
        return scpiMapper.scpiToScpiDtoOut(scpis);
    }

    public List<ScpiDtoOut> getScpiWithFilter(SearchScpiDto searchScpiDto) {
        String searchTerm = (searchScpiDto.getSearchTerm() != null && !searchScpiDto.getSearchTerm().trim().isEmpty())
                ? searchScpiDto.getSearchTerm().trim()
                : null;
        double minimumSubscription = (searchScpiDto.getMinimumSubscription() != null)
                ? searchScpiDto.getMinimumSubscription().doubleValue()
                : 0.0;
        double distributionRate = (searchScpiDto.getDistributionRate() != null)
                ? searchScpiDto.getDistributionRate().doubleValue()
                : 0.0;
        Boolean subscriptionFees = searchScpiDto.getSubscriptionFees();

        List<Scpi> scpis = scpiRepository.searchScpi(
                searchTerm,
                searchScpiDto.getLocations(),
                searchScpiDto.getSectors(),
                minimumSubscription,
                distributionRate,
                subscriptionFees
        );
        return scpis.stream()
                .map(scpiMapper::scpiToScpiDtoOut)
                .toList();
    }

    public ScpiDtoOut getScpiDetailsById(Integer id) {
        return scpiRepository.findById(id).map(scpiMapper::scpiToScpiDtoOut).orElse(null);
    }

    public List<ScpiDtoOut> getAllScpis() {
        return scpiRepository.findAll()
                .stream()
                .map(scpiMapper::scpiToScpiDtoOut)
                .toList();
    }

    public void deleteMissingScpis(Set<String> scpisInCsv) {
        List<Scpi> allScpis = scpiRepository.findAll();
        List<Scpi> scpisToDelete = allScpis.stream()
                .filter(scpi -> !scpisInCsv.contains(scpi.getName()))
                .toList();

        if (!scpisToDelete.isEmpty()) {
            log.info("Suppression de {} SCPIs absentes du fichier CSV.", scpisToDelete.size());
            scpiRepository.deleteAll(scpisToDelete);
        } else {
            log.info("Aucune SCPI à supprimer.");
        }
    }


    public List<ScpiDtoOut> getScpisWithScheduledPayment() {
        return scpiMapper.scpiToScpiDtoOut(scpiRepository.findByScheduledPaymentTrue());
    }

    public List<String> ListAllNames() {
        return scpiRepository.findAllNames();
    }

    public ScpiDtoOut getScpiDetailsByName(String scpiName) {
        return scpiRepository.findByName(scpiName).map(scpiMapper::scpiToScpiDtoOut).orElse(null);
    }
}
