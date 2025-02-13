package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.entity.Location;

import java.util.List;

public interface LocationService {
    List<Location> getLocationById(Integer id);
}
