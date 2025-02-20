package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.LocationRequest;
import fr.formationacademy.scpiinvestplusapi.entity.Location;
import fr.formationacademy.scpiinvestplusapi.entity.LocationId;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.EntityMapper;
import fr.formationacademy.scpiinvestplusapi.repository.LocationRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;
    private final EntityMapper locationMapper;

    public List<Location> createLocations(String localisationData, Scpi scpi) {
        if (StringUtils.isBlank(localisationData)) {
            log.warn("Aucune localisation fournie pour la SCPI: {}", scpi.getName());
            return Collections.emptyList();
        }

        List<Location> newLocations = parseLocations(localisationData, scpi);
        if (newLocations.isEmpty()) {
            log.warn("Aucune localisation valide créée pour la SCPI: {}", scpi.getName());
            return Collections.emptyList();
        }

        List<Location> existingLocations = locationRepository.findByScpiId(scpi.getId());
        List<LocationRequest> newLocationRequests = locationMapper.toRequestLocationList(newLocations);

        if (isSameLocation(existingLocations, newLocationRequests)) {
            log.info("Aucune modification des localisations pour la SCPI: {}", scpi.getName());
            return existingLocations;
        }

        log.info("Mise à jour des localisations pour la SCPI: {}", scpi.getName());
        return newLocations;
    }

    private List<Location> parseLocations(String localisationData, Scpi scpi) {
        String[] tokens = localisationData.split(",");
        if (tokens.length % 2 != 0) {
            log.error("Données de localisation incorrectes pour la SCPI {} : {}", scpi.getName(), localisationData);
            return Collections.emptyList();
        }

        return IntStream.range(0, tokens.length / 2)
                .mapToObj(i -> parseLocation(tokens[i * 2].trim(), tokens[i * 2 + 1].trim(), scpi))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<Location> parseLocation(String country, String percentageStr, Scpi scpi) {
        try {
            BigDecimal percentage = new BigDecimal(percentageStr).setScale(1, RoundingMode.HALF_UP);
            if (percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
                log.warn("Pourcentage invalide pour {}: {}%", country, percentage);
                return Optional.empty();
            }
            return Optional.of(new Location(new LocationId(scpi.getId(), country), percentage, scpi));
        } catch (NumberFormatException e) {
            log.error("Erreur de parsing pour la localisation: {}", percentageStr, e);
            return Optional.empty();
        }
    }


    public void saveLocations(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            log.warn("Tentative de sauvegarde d'une liste vide ou nulle de localisations.");
            return;
        }

        List<Location> validLocations = locations.stream()
                .filter(this::isValidLocation)
                .toList();

        if (validLocations.isEmpty()) {
            log.warn("Aucune localisation valide à sauvegarder.");
            return;
        }

        try {
            locationRepository.saveAll(validLocations);
            log.info("{} localisations enregistrées avec succès.", validLocations.size());
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde des localisations : {}", e.getMessage(), e);
            throw new RuntimeException("Impossible d'enregistrer les localisations", e);
        }
    }

    private boolean isValidLocation(Location location) {
        if (location == null || location.getId() == null || StringUtils.isBlank(location.getId().getCountry()) || location.getId().getScpiId() == null) {
            log.warn("Localisation invalide : clé composite incorrecte {}", location);
            return false;
        }

        if (location.getCountryPercentage() == null
                || location.getCountryPercentage().compareTo(BigDecimal.ZERO) < 0
                || location.getCountryPercentage().compareTo(BigDecimal.valueOf(100)) > 0) {
            log.warn("Localisation invalide : pourcentage incorrect {}", location);
            return false;
        }

        return true;
    }

    public boolean isSameLocation(List<Location> existingLocations, List<LocationRequest> newLocationRequests) {
        if (existingLocations.size() != newLocationRequests.size()) {
            return false;
        }

        Map<String, BigDecimal> existingMap = existingLocations.stream()
                .collect(Collectors.toMap(loc -> loc.getId().getCountry(), Location::getCountryPercentage));

        return newLocationRequests.stream().allMatch(dto ->
                existingMap.containsKey(dto.getCountry()) &&
                        existingMap.get(dto.getCountry()).compareTo(dto.getCountryPercentage()) == 0
        );
    }


}



