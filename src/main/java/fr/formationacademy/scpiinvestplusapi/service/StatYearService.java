package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.entity.StatYearId;
import fr.formationacademy.scpiinvestplusapi.mapper.EntityMapper;
import fr.formationacademy.scpiinvestplusapi.dto.StatYearRequest;
import fr.formationacademy.scpiinvestplusapi.repository.StatYearRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatYearService {
    private final StatYearRepository statYearRepository;
    private final EntityMapper statYearMapper;

    public  List<StatYear> createStatYears(ScpiDto dto, Scpi scpi) {
        if (dto.getDistributedRate() == null) {
            log.warn("Aucune donnée de taux de distribution pour '{}'", scpi.getName());
            return Collections.emptyList();
        }

        int currentYear = Year.now().getValue();
        int statYear = dto.getStatYear() != null ? dto.getStatYear() : currentYear - 1;

        StatYearId statYearId = new StatYearId(statYear, scpi.getId());
        StatYear statYearObj = StatYear.builder()
                .yearStat(statYearId)
                .distributionRate(dto.getDistributedRate())
                .sharePrice(dto.getSharePrice())
                .reconstitutionValue(dto.getReconstitutionValue())
                .scpi(scpi)
                .build();

        return Collections.singletonList(statYearObj);
    }

    private List<StatYear> parseStatYears(String tauxDistributionData, Scpi scpi) {
        String[] tokens = tauxDistributionData.split(",");
        int currentYear = Year.now().getValue();

        return IntStream.range(0, tokens.length)
                .mapToObj(i -> parseStatYear(tokens[i].trim(), scpi, currentYear - (i)))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<StatYear> parseStatYear(String tauxStr, Scpi scpi, int year) {
        try {
            float taux = Float.parseFloat(tauxStr);
            if (taux < 0) {
                log.warn("Valeur de taux invalide pour {}: {}%", year, taux);
                return Optional.empty();
            }

            return Optional.of(new StatYear(new StatYearId(year, scpi.getId()), taux, null, null, scpi));
        } catch (NumberFormatException e) {
            log.error("Erreur de parsing pour le taux de distribution: {}", tauxStr, e);
            return Optional.empty();
        }
    }

    public void saveStatYears(List<StatYear> statYears) {
        if (CollectionUtils.isEmpty(statYears)) {
            log.warn("Tentative de sauvegarde d'une liste vide ou nulle de statistiques annuelles.");
            return;
        }

        List<StatYear> validStatYears = statYears.stream()
                .filter(this::isValidStatYear)
                .collect(Collectors.toList());

        if (validStatYears.isEmpty()) {
            log.warn("Aucune donnée de statistique valide à sauvegarder.");
            return;
        }

        try {
            statYearRepository.saveAll(validStatYears);
            log.info("{} statistiques annuelles enregistrées avec succès.", validStatYears.size());
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde des statistiques annuelles : {}", e.getMessage(), e);
            throw new RuntimeException("Impossible d'enregistrer les statistiques annuelles", e);
        }
    }

    private boolean isValidStatYear(StatYear statYear) {
        return statYear != null && statYear.getYearStat() != null
                && statYear.getYearStat().getScpiId() != null
                && statYear.getDistributionRate() != null
                && statYear.getDistributionRate() >= 0;
    }

    private boolean isSameStatYears(List<StatYear> existingStatYears, List<StatYearRequest> newStatYearRequests) {
        if (existingStatYears.size() != newStatYearRequests.size()) {
            return false;
        }

        Map<Integer, Float> existingMap = existingStatYears.stream()
                .collect(Collectors.toMap(stat -> stat.getYearStat().getYearStat(), StatYear::getDistributionRate));

        return newStatYearRequests.stream().allMatch(dto ->
                existingMap.getOrDefault(dto.getYearStat(), -1f).equals(dto.getDistributionRate()));
    }
}
