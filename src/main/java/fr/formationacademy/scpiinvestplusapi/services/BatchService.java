package fr.formationacademy.scpiinvestplusapi.services;


import fr.formationacademy.scpiinvestplusapi.model.dto.BatchDataDto;
import fr.formationacademy.scpiinvestplusapi.model.dto.requests.ScpiDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface BatchService {

    @Transactional
    void saveOrUpdateBatchData(List<BatchDataDto> batchDataList);

    BatchDataDto convertToBatchData(ScpiDto scpiDto);



}
