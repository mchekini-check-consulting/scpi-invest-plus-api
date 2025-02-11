package fr.formationacademy.scpiinvestplusapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import fr.formationacademy.scpiinvestplusapi.dto.DetailsDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.services.ScpiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/application/Scpi")
public class ScpiController {
    @Autowired
    private final ScpiService scpiService;
    
    public ScpiController(ScpiService scpiService){
        this.scpiService = scpiService;
    }

    @GetMapping("/details/{scpid}")
    public ResponseEntity<DetailsDTO> getMethodName(@PathVariable int scpid) {
        return ResponseEntity.ok( this.scpiService.gettAllDetails( scpid));
    }
    
}
