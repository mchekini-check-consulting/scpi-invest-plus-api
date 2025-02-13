package fr.formationacademy.scpiinvestplusapi.service.impl;

import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.repository.LocationRepository;
import fr.formationacademy.scpiinvestplusapi.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    @Override
    public List<Location> getLocationById(Integer id) {
        return this.locationRepository.findLocationsByScpi_Id(id);
    }
}
