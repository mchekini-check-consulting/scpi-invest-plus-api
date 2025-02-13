package fr.formationacademy.scpiinvestplusapi.batch.writer;

import fr.formationacademy.scpiinvestplusapi.model.dto.BatchDataDto;
import fr.formationacademy.scpiinvestplusapi.services.BatchService;
import fr.formationacademy.scpiinvestplusapi.services.LocationService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BatchItemWriter implements ItemWriter<BatchDataDto> {

    private final BatchService batchService;
    private final LocationService locationService;

    @Transactional
    @Override
    public void write(Chunk<? extends BatchDataDto> items) {
        if (items.isEmpty()) return;

        List<BatchDataDto> batchDataList = items.getItems().stream()
                .map(item -> (BatchDataDto) item)
                .collect(Collectors.toList());

        batchService.saveOrUpdateBatchData(batchDataList);

        batchDataList.forEach(batchData -> {
            if (batchData.getLocations() != null) {
                locationService.saveLocations(batchData.getLocations());
            }
        });
    }
}
