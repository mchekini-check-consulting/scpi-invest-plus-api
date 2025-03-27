package fr.formationacademy.scpiinvestplusapi.service;


import fr.formationacademy.scpiinvestplusapi.entity.ScpiIndex;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiIndexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScpiIndexService {

    private final ScpiIndexRepository scpiSearchRepository;


    public ScpiIndexService(ScpiIndexRepository scpiSearchRepository) {
        this.scpiSearchRepository = scpiSearchRepository;
    }

    public ScpiIndex saveScpiSearch(ScpiIndex scpiSearch) {
        return scpiSearchRepository.save(scpiSearch);
    }

    public Iterable<ScpiIndex> saveMultipleScpiSearch(List<ScpiIndex> scpiSearchList) {
        return scpiSearchRepository.saveAll(scpiSearchList);
    }

}
