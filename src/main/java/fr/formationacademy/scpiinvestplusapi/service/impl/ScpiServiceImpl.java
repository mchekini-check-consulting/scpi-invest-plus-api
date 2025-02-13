package fr.formationacademy.scpiinvestplusapi.service.impl;


import fr.formationacademy.scpiinvestplusapi.dto.StatYearDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiMapper;
import fr.formationacademy.scpiinvestplusapi.service.ScpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;

import java.util.List;

@Service
public class ScpiServiceImpl implements ScpiService {
    
    private final ScpiRepository scpiRepository;


    private final ScpiMapper scpiMapper;


    public ScpiServiceImpl(ScpiRepository scpiRepository, ScpiMapper scpiMapper) {
        this.scpiRepository = scpiRepository;
        this.scpiMapper = scpiMapper;
    }

        /* public ScpiDTO gettAllDetails(int scpid){

       return this.scpiRepository.getScpiDetails(scpid);
    }*/

    @Override
    public ScpiDTO addScpi(ScpiDTO scpiDTO) {
        Scpi scpi = scpiMapper.toEntity(scpiDTO);
        Scpi savedScpi = scpiRepository.save(scpi);
        return scpiMapper.toDTO(savedScpi);
    }

    @Override
    public ScpiDTO getScpiById(Integer id) {
        return null;
    }

    @Override
    public List<ScpiDTO> findAllScpi() {
        return null;
    }

    @Override
    public ScpiDTO saveOrUpdateScpi(ScpiDTO scpiDTO) {
        return null;
    }

    @Override
    public void deleteScpi(Integer id) {

    }

    @Override
    public List<StatYearDTO> getStatYearsForScpi(Integer scpiId) {
        return null;
    }


}
