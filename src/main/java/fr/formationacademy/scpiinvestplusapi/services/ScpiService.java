package fr.formationacademy.scpiinvestplusapi.services;


import org.springframework.stereotype.Service;

import fr.formationacademy.scpiinvestplusapi.dto.DetailsDTO;
import fr.formationacademy.scpiinvestplusapi.repositories.ScpiRepository;

@Service
public class ScpiService {
    
    private final ScpiRepository scpiRepository;

    public ScpiService(ScpiRepository scpiRepository) {
        this.scpiRepository = scpiRepository;
    }
    
    public DetailsDTO gettAllDetails( int scpid){
       
       return this.scpiRepository.getScpiDetails(scpid);
    }

     
}
