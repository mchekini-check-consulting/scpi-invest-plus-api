package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;

import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;
import fr.formationacademy.scpiinvestplusapi.mapper.RefDismembermentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.RefDismembermentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RefDismembermentService {

    private final RefDismembermentRepository repository;
    private final RefDismembermentMapper mapper;

    public RefDismembermentService(RefDismembermentRepository repository, RefDismembermentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RefDismembermentDto> getByPropertyType(String typeProperty) {
        log.info("Début de la récupération des dismemberments pour le type de propriété: {}", typeProperty);
        List<RefDismemberment> entities = repository.findByPropertyType(typeProperty);
        if (entities.isEmpty()) {
            log.warn("Aucun dismemberment trouvé pour le type de propriété: {}", typeProperty);
        } else {
            log.info("{} dismemberments trouvés pour le type de propriété: {}", entities.size(), typeProperty);
        }
        List<RefDismembermentDto> dtoList = mapper.toDTOList(entities);
        log.info("Fin de la récupération des dismemberments pour le type de propriété: {}", typeProperty);
        return dtoList;
    }
}
