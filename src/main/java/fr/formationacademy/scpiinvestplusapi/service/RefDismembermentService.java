package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;
import fr.formationacademy.scpiinvestplusapi.entity.RefDismemberment;
import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import fr.formationacademy.scpiinvestplusapi.mapper.RefDismembermentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.RefDismembermentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefDismembermentService {

    private final RefDismembermentRepository repository;
    private final RefDismembermentMapper repositoryMapper;

    public RefDismembermentService(RefDismembermentRepository repository) {
        this.repository = repository;
        this.repositoryMapper = null;
    }

    public List<RefDismembermentDto> getByPropertyType(PropertyType typeProperty) {
        List<RefDismemberment> entities = repository.findByPropertyType(typeProperty);
        return repositoryMapper.toDTOList(entities);
    }

}
