package fr.formationacademy.scpiinvestplusapi.batch.processor;

import fr.formationacademy.scpiinvestplusapi.model.dto.BatchDataDto;
import fr.formationacademy.scpiinvestplusapi.model.dto.requests.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.model.entiry.*;
import fr.formationacademy.scpiinvestplusapi.repositories.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.services.LocationService;
import io.micrometer.common.lang.NonNull;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScpiItemProcessor implements ItemProcessor<BatchDataDto, Scpi> {

    private final ScpiRepository scpiRepository;
    private final LocationService locationService;
    private final Map<String, Scpi> existingScpis = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("Chargement des SCPIs existantes...");
        List<Scpi> scpis = scpiRepository.findAll();
        scpis.forEach(scpi -> existingScpis.put(scpi.getName(), scpi));
        log.info("Nombre de SCPIs existantes chargées: {}", existingScpis.size());
    }

    @Override
    public Scpi process(@NonNull BatchDataDto batchDataDto) {
        log.info("Processing BatchDataDto: {}", batchDataDto);

        ScpiDto dto = batchDataDto.getScpiDto();
        if (dto == null || dto.getName() == null) {
            log.warn("SCPI invalide, ignorée.");
            return null;
        }

        Scpi existingScpi = existingScpis.get(dto.getName());

        if (existingScpi != null && isSame(existingScpi, dto)) {
            log.info("SCPI '{}' déjà existante et inchangée, ignorée.", dto.getName());
            return null;
        }

        Scpi scpi = createOrUpdateScpi(dto, existingScpi);
        List<Location> locations = locationService.createLocations(dto.getLocation(), scpi);
        locationService.saveLocations(locations);
        scpi.setLocations(locations);

        scpi.setSectors(batchDataDto.getSectors() != null ? new ArrayList<>(batchDataDto.getSectors()) : new ArrayList<>());
        scpi.setStatYears(createStatYears(batchDataDto.getStatYears(), scpi));

        return scpi;
    }

    private Scpi createOrUpdateScpi(ScpiDto dto, Scpi existingScpi) {
        Scpi scpi = (existingScpi != null) ? existingScpi : new Scpi();
        scpi.setName(dto.getName());
        scpi.setMinimumSubscription(dto.getMinimumSubscription());
        scpi.setCapitalization(dto.getCapitalization());
        scpi.setManager(dto.getManager());
        scpi.setSubscriptionFees(dto.getSubscriptionFees());
        scpi.setManagementCosts(dto.getManagementCosts());
        scpi.setEnjoymentDelay(dto.getEnjoymentDelay());
        scpi.setIban(dto.getIban());
        scpi.setBic(dto.getBic());
        scpi.setScheduledPayment(dto.getScheduledPayment());
        scpi.setCashback(dto.getCashback());
        scpi.setAdvertising(dto.getAdvertising());
        return scpi;
    }

    private List<StatYear> createStatYears(List<StatYear> statYearsDto, Scpi scpi) {
        if (statYearsDto == null || statYearsDto.isEmpty()) {
            return new ArrayList<>();
        }
        return statYearsDto.stream()
                .map(statYear -> {
                    StatYear newStatYear = new StatYear();
                    newStatYear.setId(new StatYearKey(2024, null));
                    newStatYear.setDistributionRate(statYear.getDistributionRate());
                    newStatYear.setSharePrice(statYear.getSharePrice());
                    newStatYear.setReconstitutionValue(statYear.getReconstitutionValue());
                    newStatYear.setScpi(scpi);
                    return newStatYear;
                })
                .collect(Collectors.toList());
    }

    private boolean isSame(Scpi existing, ScpiDto dto) {
        return Objects.equals(existing.getMinimumSubscription(), dto.getMinimumSubscription())
                && Objects.equals(existing.getCapitalization(), dto.getCapitalization())
                && Objects.equals(existing.getManager(), dto.getManager())
                && Objects.equals(existing.getSubscriptionFees(), dto.getSubscriptionFees())
                && Objects.equals(existing.getManagementCosts(), dto.getManagementCosts())
                && Objects.equals(existing.getEnjoymentDelay(), dto.getEnjoymentDelay())
                && Objects.equals(existing.getIban(), dto.getIban())
                && Objects.equals(existing.getBic(), dto.getBic())
                && Objects.equals(existing.getScheduledPayment(), dto.getScheduledPayment())
                && Objects.equals(existing.getCashback(), dto.getCashback())
                && Objects.equals(existing.getAdvertising(), dto.getAdvertising());
    }
}



