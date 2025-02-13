package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;

import java.util.List;

public interface ScpiService {


    ScpiDTO addScpi(ScpiDTO scpiDTO);


    ScpiDTO getScpiById(Integer id);


    List<ScpiDTO> findAllScpi();


    ScpiDTO saveOrUpdateScpi(ScpiDTO scpiDTO);

    ScpiDTO getScpiDetailsById(Integer id);

    void deleteScpi(Integer id);


}
