package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SectorService {
    List<Sector> getSectorsForScpi(Integer scpiId);
}
