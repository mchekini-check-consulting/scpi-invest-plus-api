package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.repository.StatYearRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatYearService {
    private final StatYearRepository statYearRepository;

    public StatYearService(StatYearRepository statYearRepository) {
        this.statYearRepository = statYearRepository;
    }
    public List<StatYear> getStatYearsByScpiNames(List<String> scpiNames) {
        return statYearRepository.findByScpi_NameIn(scpiNames);
    }
}
