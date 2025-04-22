package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.repository.SectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public List<Sector> getSectorsByScpiNames(List<String> scpiNames) {
        return sectorRepository.findByScpi_NameIn(scpiNames);
    }
}
