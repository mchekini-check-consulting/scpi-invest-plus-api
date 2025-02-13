package fr.formationacademy.scpiinvestplusapi.service;


import fr.formationacademy.scpiinvestplusapi.entity.StatYear;

import java.util.List;

public interface StatYearService {
    List<StatYear> getStatYearsForScpi(Integer scpiId);
}
