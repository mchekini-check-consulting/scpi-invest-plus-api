package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.repository.StatYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatYearService {
    @Autowired
    private final StatYearRepository statYearRepository;

    public StatYearService(StatYearRepository statYearRepository) {
        this.statYearRepository = statYearRepository;
    }

    public List<StatYear> getStatYearsForScpi(Integer scpiId) {
        return this.statYearRepository.findByScpi_Id(scpiId);
    }


}
