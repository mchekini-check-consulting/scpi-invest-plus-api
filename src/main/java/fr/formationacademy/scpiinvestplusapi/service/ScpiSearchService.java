package fr.formationacademy.scpiinvestplusapi.service;


import fr.formationacademy.scpiinvestplusapi.entity.ScpiSearch;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScpiSearchService {

    private final ScpiSearchRepository scpiSearchRepository;


    public ScpiSearchService(ScpiSearchRepository scpiSearchRepository) {
        this.scpiSearchRepository = scpiSearchRepository;
    }

    public ScpiSearch saveScpiSearch(ScpiSearch scpiSearch) {
        return scpiSearchRepository.save(scpiSearch);
    }

    public Iterable<ScpiSearch> saveMultipleScpiSearch(List<ScpiSearch> scpiSearchList) {
        return scpiSearchRepository.saveAll(scpiSearchList);
    }

}
