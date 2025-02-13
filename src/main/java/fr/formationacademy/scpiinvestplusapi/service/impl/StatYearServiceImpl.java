package fr.formationacademy.scpiinvestplusapi.service.impl;

import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
import fr.formationacademy.scpiinvestplusapi.repository.StatYearRepository;
import fr.formationacademy.scpiinvestplusapi.service.StatYearService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StatYearServiceImpl implements StatYearService {
    private final StatYearRepository statYearRepository ;

    public StatYearServiceImpl(StatYearRepository statYearRepository) {
        this.statYearRepository = statYearRepository;
    }



}
