package fr.formationacademy.scpiinvestplusapi.services;

import fr.formationacademy.scpiinvestplusapi.dto.SectorRequest;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.Sector;
import fr.formationacademy.scpiinvestplusapi.entity.SectorId;
import fr.formationacademy.scpiinvestplusapi.mapper.EntityMapper;
import fr.formationacademy.scpiinvestplusapi.repository.SectorRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectorService{

    private final SectorRepository sectorRepository;
    private final EntityMapper sectorMapper;

    public List<Sector> createSectors(String sectorData, Scpi scpi) {
        if (StringUtils.isBlank(sectorData)) {
            log.warn("Aucun secteur fourni pour la SCPI: {}", scpi.getName());
            return Collections.emptyList();
        }

        List<Sector> newSectors = parseSectors(sectorData, scpi);
        if (newSectors.isEmpty()) {
            log.warn("Aucun secteur valide créé pour la SCPI: {}", scpi.getName());
            return Collections.emptyList();
        }

        List<Sector> existingSectors = sectorRepository.findByScpiId(scpi.getId());
        List<SectorRequest> newSectorRequests = sectorMapper.toRequestSectorList(newSectors);

        if (isSameSector(existingSectors, newSectorRequests)) {
            log.info("Aucune modification des secteurs pour la SCPI: {}", scpi.getName());
            return existingSectors;
        }

        log.info("Mise à jour des secteurs pour la SCPI: {}", scpi.getName());
        return newSectors;
    }

    private List<Sector> parseSectors(String sectorData, Scpi scpi) {
        String[] tokens = sectorData.split(",");
        if (tokens.length % 2 != 0) {
            log.error("Données de secteur incorrectes pour la SCPI {} : {}", scpi.getName(), sectorData);
            return Collections.emptyList();
        }

        return IntStream.range(0, tokens.length / 2)
                .mapToObj(i -> parseSector(tokens[i * 2].trim(), tokens[i * 2 + 1].trim(), scpi))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<Sector> parseSector(String name, String percentageStr, Scpi scpi) {
        try {
            float percentage = Float.parseFloat(percentageStr);
            if (percentage < 0 || percentage > 100) {
                log.warn("Pourcentage invalide pour {}: {}%", name, percentage);
                return Optional.empty();
            }
            return Optional.of(new Sector(new SectorId(name, scpi.getId()), percentage, scpi));
        } catch (NumberFormatException e) {
            log.error("Erreur de parsing pour le secteur: {}", percentageStr, e);
            return Optional.empty();
        }
    }

    public void saveSectors(List<Sector> sectors) {
        if (sectors == null || sectors.isEmpty()) {
            log.warn("Tentative de sauvegarde d'une liste vide ou nulle de secteurs.");
            return;
        }

        List<Sector> validSectors = sectors.stream()
                .filter(this::isValidSector)
                .toList();

        if (validSectors.isEmpty()) {
            log.warn("Aucun secteur valide à sauvegarder.");
            return;
        }

        try {
            sectorRepository.saveAll(validSectors);
            log.info("{} secteurs enregistrés avec succès.", validSectors.size());
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde des secteurs : {}", e.getMessage(), e);
            throw new RuntimeException("Impossible d'enregistrer les secteurs", e);
        }
    }

    private boolean isValidSector(Sector sector) {
        if (sector == null || sector.getId() == null || StringUtils.isBlank(sector.getId().getName()) || sector.getId().getScpiId() == null) {
            log.warn("Secteur invalide : clé composite incorrecte {}", sector);
            return false;
        }

        if (sector.getSectorPercentage() == null || sector.getSectorPercentage() < 0 || sector.getSectorPercentage() > 100) {
            log.warn("Secteur invalide : pourcentage incorrect {}", sector);
            return false;
        }

        return true;
    }

    public boolean isSameSector(List<Sector> existingSectors, List<SectorRequest> newSectorRequests) {
        if (existingSectors.size() != newSectorRequests.size()) {
            return false;
        }

        Map<String, Float> existingMap = existingSectors.stream()
                .collect(Collectors.toMap(sector -> sector.getId().getName(), Sector::getSectorPercentage));

        return newSectorRequests.stream().allMatch(dto ->
                existingMap.containsKey(dto.getName()) &&
                        existingMap.get(dto.getName()).equals(dto.getSectorPercentage())
        );
    }
}
