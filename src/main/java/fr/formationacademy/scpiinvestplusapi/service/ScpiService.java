package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import org.springframework.stereotype.Service;

@Service
public class ScpiService {

    private final ScpiRepository scpiRepository;


    private final ScpiMapper scpiMapper;


    public ScpiService(ScpiRepository scpiRepository, ScpiMapper scpiMapper) {
        this.scpiRepository = scpiRepository;
        this.scpiMapper = scpiMapper;
    }


    public ScpiDTO getScpiDetailsById(Integer id) {
        return scpiRepository.findById(id).map(scpiMapper::toDTO).orElse(null);
    }


}
