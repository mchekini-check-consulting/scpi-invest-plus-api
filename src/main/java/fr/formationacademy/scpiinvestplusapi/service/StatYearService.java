package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.entity.StatYearId;
import fr.formationacademy.scpiinvestplusapi.mapper.EntityMapper;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearRequest;
import fr.formationacademy.scpiinvestplusapi.repository.StatYearRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatYearService {

    private final StatYearRepository statYearRepository;
    private final EntityMapper statYearmapper;

    public List<StatYear> createStatYears(List<StatYear> statYearData, Scpi scpi) {
        if (statYearData == null || statYearData.isEmpty()) {
            log.warn("Aucune donnée statistique fournie pour la SCPI: {}", scpi.getName());
            return Collections.emptyList();
        }

        List<StatYear> existingStatYears = statYearRepository.findByScpiId(scpi.getId());

        if (isSameStatYears(existingStatYears, statYearData)) {
            log.info("Aucune modification des statistiques pour la SCPI: {}", scpi.getName());
            return existingStatYears;
        }

        log.info("Mise à jour des statistiques pour la SCPI: {}", scpi.getName());
        return statYearData;
    }


    public List<StatYear> parseStatYears(String distributionRates, Scpi scpi) {
        log.info("Début du parsing des taux de distribution pour la SCPI: {}", scpi.getId());

        if (distributionRates == null || distributionRates.isBlank()) {
            log.warn("Aucun taux de distribution fourni.");
            return Collections.emptyList();
        }

        AtomicInteger currentYear = new AtomicInteger(Year.now().getValue());
        log.debug("Année actuelle récupérée: {}", currentYear);

        List<StatYear> statYears = Arrays.stream(distributionRates.split(",+"))
                .map(rateStr -> {
                    try {
                        float rate = Float.parseFloat(rateStr);
                        int year = currentYear.getAndDecrement();
                        log.debug("Taux {} associé à l'année {}", rate, year);
                        return new StatYear(new StatYearId(year, scpi.getId()), rate, null, null, scpi);
                    } catch (NumberFormatException e) {
                        log.warn("Taux de distribution invalide: {}", rateStr);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        log.info("Parsing terminé, {} entrées générées.", statYears.size());
        return statYears;
    }


    public void saveStatYears(List<StatYear> statYears) {
        if (statYears == null || statYears.isEmpty()) {
            log.warn("Tentative de sauvegarde d'une liste vide ou nulle de statistiques.");
            return;
        }

        try {
            statYearRepository.saveAll(statYears);
            log.info("{} statistiques enregistrées avec succès.", statYears.size());
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde des statistiques : {}", e.getMessage(), e);
            throw new RuntimeException("Impossible d'enregistrer les statistiques", e);
        }
    }

    public boolean isSameStatYears(List<StatYear> existingStatYears, List<StatYear> newStatYears) {
        if (existingStatYears.size() != newStatYears.size()) {
            return false;
        }

        return new HashSet<>(existingStatYears).containsAll(newStatYears) && new HashSet<>(newStatYears).containsAll(existingStatYears);
    }

    public List<StatYear> mapToStatYears(List<StatYearRequest> requests) {
        return requests.stream()
                .map(statYearmapper::toStatYear)
                .toList();
    }


}