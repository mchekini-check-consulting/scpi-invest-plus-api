package fr.formationacademy.scpiinvestplusapi.services;

import fr.formationacademy.scpiinvestplusapi.model.entiry.Location;
import fr.formationacademy.scpiinvestplusapi.model.entiry.Scpi;

import java.util.List;

public interface LocationService {
    List<Location> createLocations(String locationString, Scpi scpi);

    void saveLocations(List<Location> locations);
}
