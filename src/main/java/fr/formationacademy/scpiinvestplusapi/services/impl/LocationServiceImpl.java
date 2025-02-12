package fr.formationacademy.scpiinvestplusapi.services.impl;

import fr.formationacademy.scpiinvestplusapi.model.entiry.Location;
import fr.formationacademy.scpiinvestplusapi.model.entiry.LocationKey;
import fr.formationacademy.scpiinvestplusapi.model.entiry.Scpi;
import fr.formationacademy.scpiinvestplusapi.repositories.LocationRepository;
import fr.formationacademy.scpiinvestplusapi.services.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;


    @Override
    public List<Location> createLocations(String locationString, Scpi scpi) {
        if (locationString == null || locationString.isEmpty()) {
            return new ArrayList<>();
        }

        List<Location> locations = new ArrayList<>();
        String[] countries = locationString.split(",");
        float percentage = 100.0f / countries.length;

        for (String country : countries) {
            country = country.trim();
            LocationKey locationKey = new LocationKey(country, scpi.getId());
            Location location = new Location();
            location.setId(locationKey);
            location.setScpi(scpi);
            location.setCountryPercentage(percentage);
            locations.add(location);
        }

        return locations;
    }

    @Override
    public void saveLocations(List<Location> locations) {
        if (!locations.isEmpty()) {
            locationRepository.saveAll(locations);
            log.info("Saved {} locations to the database.", locations.size());
        }
    }
}

