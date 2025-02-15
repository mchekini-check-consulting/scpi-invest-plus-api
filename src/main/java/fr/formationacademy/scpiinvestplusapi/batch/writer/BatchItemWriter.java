package fr.formationacademy.scpiinvestplusapi.batch.writer;

import fr.formationacademy.scpiinvestplusapi.dto.BatchDataDto;

import fr.formationacademy.scpiinvestplusapi.service.BatchService;
import fr.formationacademy.scpiinvestplusapi.service.LocationService;
import fr.formationacademy.scpiinvestplusapi.service.SectorService;
import fr.formationacademy.scpiinvestplusapi.service.StatYearService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchItemWriter implements ItemWriter<BatchDataDto> {

    private final BatchService batchService;
    private final LocationService locationService;
    private final SectorService sectorService;
    private final StatYearService statYearService;

    @Transactional
    @Override
    public void write(Chunk<? extends BatchDataDto> items) {
        if (items.isEmpty()) return;

        List<BatchDataDto> batchDataList = items.getItems().stream()
                .map(item -> (BatchDataDto) item)
                .toList();

        batchService.saveOrUpdateBatchData(batchDataList);

        batchDataList.forEach(batchData -> {
            if (batchData.getLocations() != null) {
                locationService.saveLocations(batchData.getLocations());
            }
            if (batchData.getSectors() != null) {
                sectorService.saveSectors(batchData.getSectors());
            }
            if (batchData.getStatYears() != null) {
                statYearService.saveStatYears(batchData.getStatYears());
            }
        });

    }
}
