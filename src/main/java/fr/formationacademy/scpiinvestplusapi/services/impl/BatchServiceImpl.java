package fr.formationacademy.scpiinvestplusapi.services.impl;

import fr.formationacademy.scpiinvestplusapi.batch.processor.ScpiItemProcessor;
import fr.formationacademy.scpiinvestplusapi.mappers.EntityMapper;
import fr.formationacademy.scpiinvestplusapi.model.dto.BatchDataDto;
import fr.formationacademy.scpiinvestplusapi.model.dto.requests.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.model.entiry.*;
import fr.formationacademy.scpiinvestplusapi.repositories.*;
import fr.formationacademy.scpiinvestplusapi.services.BatchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchServiceImpl implements BatchService {

    private final InvestorRepository investorRepository;
    private final ScpiRepository scpiRepository;
    private final EntityMapper entityMapper;
    private final ScpiItemProcessor processor;

    @Transactional
    @Override
    public void saveOrUpdateBatchData(List<BatchDataDto> batchDataList) {
        if (batchDataList == null || batchDataList.isEmpty()) {
            log.warn("Batch data list is empty. Skipping processing.");
            return;
        }

        log.info("Processing {} batch items...", batchDataList.size());

        Map<String, Scpi> existingScpis = getExistingScpis(batchDataList);
        Map<String, Investor> existingInvestors = getExistingInvestors(batchDataList);

        List<Scpi> scpisToInsert = new ArrayList<>();
        List<Scpi> scpisToUpdate = new ArrayList<>();
        List<Investor> investorsToSave = new ArrayList<>();

        /*for (BatchDataDto dto : batchDataList) {
            processor.processInvestor(dto.getInvestor(), existingInvestors, investorsToSave);
            processor.processScpi(dto.getScpi(), existingScpis, scpisToInsert, scpisToUpdate);
        }*/

        saveEntities(investorRepository, investorsToSave, "Investors");
        saveEntities(scpiRepository, scpisToInsert, "New SCPIs");
        saveEntities(scpiRepository, scpisToUpdate, "Updated SCPIs");
    }
    private Map<String, Scpi> getExistingScpis(List<BatchDataDto> batchDataList) {
        List<String> scpiNames = batchDataList.stream()
                .map(dto -> dto.getScpi().getName())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return scpiRepository.findByNameIn(scpiNames)
                .stream().collect(Collectors.toMap(Scpi::getName, scpi -> scpi));
    }

    private Map<String, Investor> getExistingInvestors(List<BatchDataDto> batchDataList) {
        List<String> investorEmails = batchDataList.stream()
                .map(dto -> dto.getInvestor().getEmail())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return investorRepository.findByEmailIn(investorEmails)
                .stream().collect(Collectors.toMap(Investor::getEmail, investor -> investor));
    }


    private <T> void saveEntities(JpaRepository<T, ?> repository, List<T> entities, String entityName) {
        if (!entities.isEmpty()) {
            repository.saveAll(entities);
            log.info("{} entities saved: {}", entityName, entities.size());
        }
    }


    @Override
    public BatchDataDto convertToBatchData(ScpiDto request) {
        return BatchDataDto.builder()
                .scpiDto(request)
                .investor(new Investor())
                .scpi(new Scpi())
                .locations(new ArrayList<>())
                .sectors(new ArrayList<>())
                .statYears(new ArrayList<>())
                .build();
    }
}
