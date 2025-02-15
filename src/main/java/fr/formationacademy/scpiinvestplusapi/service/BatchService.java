package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.batch.processor.ScpiItemProcessor;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.EntityMapper;
import fr.formationacademy.scpiinvestplusapi.dto.BatchDataDto;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchService {

    private final ScpiRepository scpiRepository;
    private final EntityMapper entityMapper;
    private final ScpiItemProcessor processor;

    @Transactional
    public void saveOrUpdateBatchData(List<BatchDataDto> batchDataList) {
        if (batchDataList == null || batchDataList.isEmpty()) {
            log.warn("Batch data list is empty. Skipping processing.");
            return;
        }

        log.info("Processing {} batch items...", batchDataList.size());

        Map<String, Scpi> existingScpis = getExistingScpis(batchDataList);
        List<Scpi> scpisToInsert = new ArrayList<>();
        List<Scpi> scpisToUpdate = new ArrayList<>();

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


    private <T> void saveEntities(JpaRepository<T, ?> repository, List<T> entities, String entityName) {
        if (!entities.isEmpty()) {
            repository.saveAll(entities);
            log.info("{} entities saved: {}", entityName, entities.size());
        }
    }


    public BatchDataDto convertToBatchData(ScpiDto request) {
        return BatchDataDto.builder()
                .scpiDto(request)
                .scpi(new Scpi())
                .locations(new ArrayList<>())
                .sectors(new ArrayList<>())
                .statYears(new ArrayList<>())
                .build();
    }
}