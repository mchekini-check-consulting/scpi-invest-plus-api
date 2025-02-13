package fr.formationacademy.scpiinvestplusapi.service.impl;

import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.repository.SectorRepository;
import fr.formationacademy.scpiinvestplusapi.service.SectorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorServiceImpl implements SectorService {
    private final SectorRepository sectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    @Override
    public List<Sector> getSectorsForScpi(Integer scpiId) {
        return this.sectorRepository.findSectorsByScpi_Id(scpiId);
    }
}
