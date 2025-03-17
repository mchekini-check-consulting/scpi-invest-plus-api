package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;

import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.RefDismembermentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.RefDismembermentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    public List<RefDismembermentDto> getByPropertyType(PropertyType typeProperty) throws GlobalException {
        log.info("Début de la récupération des démembrements pour le type de propriété: {}", typeProperty);
        List<RefDismemberment> entities = repository.findByPropertyType(typeProperty);
        if (entities.isEmpty()) {
            log.debug("Aucun dismemberment trouvé pour le type de propriété: {}", typeProperty);
            throw new GlobalException(HttpStatus.NOT_FOUND,"No SCPI found with Property Type : " + typeProperty.getValue());
        } else {
            log.info("{} démembrements trouvés pour le type de propriété: {}", entities.size(), typeProperty);
        }
        log.info("Fin de la récupération des démembrements pour le type de propriété: {}", typeProperty);
        return mapper.toDTOList(entities);
    }
}
