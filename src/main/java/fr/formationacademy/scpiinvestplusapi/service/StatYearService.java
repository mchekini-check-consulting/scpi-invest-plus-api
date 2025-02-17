package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.entity.StatYearId;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.repository.StatYearRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatYearService {

    private final StatYearRepository statYearRepository;
    private final ScpiRepository scpiRepository;

    public List<StatYear> createStatYears(ScpiDto scpiDto, Scpi scpi) {
        List<StatYear> statYears = new ArrayList<>();

        if (scpiDto.getDistributedRate() == null || scpiDto.getReconstitutionValue() == null || scpiDto.getSharePrice() == null) {
            log.warn("Aucune donnée pour '{}'", scpi.getName());
            return Collections.emptyList();
        }

        int currentYear = Year.now().getValue();
        String[] tauxDistributionArray = scpiDto.getDistributedRate().split(",");
        String[] reconstitutionArray = scpiDto.getReconstitutionValue().split(",");
        String[] sharePriceArray = scpiDto.getSharePrice().split(",");

        int maxLength = Math.max(tauxDistributionArray.length, Math.max(reconstitutionArray.length, sharePriceArray.length));

        for (int i = 0; i < maxLength; i++) {
            int year = currentYear - i;
            StatYearId yearStatId = new StatYearId(year, scpi.getId());
            if (statYearExists(yearStatId)) {
                log.warn("StatYear déjà existant pour yearStat={} et scpiId={}", year, scpi.getId());
                continue;
            }
            float taux = (i < tauxDistributionArray.length) ? Float.parseFloat(tauxDistributionArray[i].trim()) : 0f;
            float reconstitution = (i < reconstitutionArray.length) ? Float.parseFloat(reconstitutionArray[i].trim()) : 0f;
            float sharePrice = (i < sharePriceArray.length) ? Float.parseFloat(sharePriceArray[i].trim()) : 0f;

            if (taux < 0) {
                log.warn("Taux de distribution invalide pour l'année {}: {}%", year, taux);
                continue;
            }
            StatYear statYearObj = StatYear.builder()
                    .yearStat(yearStatId)
                    .distributionRate(taux)
                    .reconstitutionValue(reconstitution)
                    .sharePrice(sharePrice)
                    .scpi(scpi)
                    .build();
            statYears.add(statYearObj);
        }
        return statYears;
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

    private boolean statYearExists(StatYearId yearStatId) {
        return statYearRepository.existsByYearStat(yearStatId);
    }

}
