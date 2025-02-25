package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.entity.StatYear;
import fr.formationacademy.scpiinvestplusapi.entity.StatYearId;
import fr.formationacademy.scpiinvestplusapi.repository.StatYearRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatYearService {

    private final StatYearRepository statYearRepository;

    public List<StatYear> createStatYears(ScpiDto scpiDto, Scpi scpi) {
        List<StatYear> statYears = new ArrayList<>();

        if (scpiDto.getDistributedRate() == null && scpiDto.getReconstitutionValue() == null && scpiDto.getSharePrice() == null) {
            log.warn("Aucune donnée disponible pour '{}'", scpi.getName());
            return Collections.emptyList();
        }

        int currentYear = Year.now().getValue();

        String[] tauxDistributionArray = (scpiDto.getDistributedRate() != null) ? scpiDto.getDistributedRate().split(",") : new String[0];
        String[] reconstitutionArray = (scpiDto.getReconstitutionValue() != null) ? scpiDto.getReconstitutionValue().split(",") : new String[0];
        String[] sharePriceArray = (scpiDto.getSharePrice() != null) ? scpiDto.getSharePrice().split(",") : new String[0];
        int maxLength = Math.max(tauxDistributionArray.length, Math.max(reconstitutionArray.length, sharePriceArray.length));

        for (int i = 0; i < maxLength; i++) {
            int year = (currentYear - 1) - i;
            StatYearId yearStatId = new StatYearId(year, scpi.getId());

            if (statYearExists(yearStatId)) {
                log.warn("StatYear déjà existant pour yearStat={} et scpiId={}", year, scpi.getId());
                continue;
            }

            BigDecimal taux = parseBigDecimal(tauxDistributionArray, i);
            BigDecimal reconstitution = parseBigDecimal(reconstitutionArray, i);
            BigDecimal sharePrice = parseBigDecimal(sharePriceArray, i);

            if (taux == null && reconstitution == null && sharePrice == null) {
                log.warn("Aucune donnée valide pour l'année {} et la SCPI '{}'", year, scpi.getName());
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

    private BigDecimal parseBigDecimal(String[] array, int index) {
        if (index >= array.length || array[index].trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(array[index].trim()).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            log.error("Erreur de format pour la valeur '{}' à l'index {}", array[index], index, e);
            return null;
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
        return statYear != null
                && statYear.getYearStat() != null
                && statYear.getYearStat().getScpiId() != null
                && statYear.getDistributionRate() != null
                && statYear.getDistributionRate().compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean statYearExists(StatYearId yearStatId) {
        return statYearRepository.existsByYearStat(yearStatId);
    }

}
