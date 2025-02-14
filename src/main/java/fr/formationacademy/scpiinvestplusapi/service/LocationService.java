package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getLocationById(Integer id) {
        return this.locationRepository.findLocationsByScpi_Id(id);
    }
}
