package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.RefDismembermentDto;

import fr.formationacademy.scpiinvestplusapi.mapper.RefDismembermentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.RefDismembermentRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j // Active le logger
@Service
public class RefDismembermentService {

    private final RefDismembermentRepository repository;
    private final RefDismembermentMapper repositoryMapper;

    @Autowired
    public RefDismembermentService(RefDismembermentRepository repository, RefDismembermentMapper repositoryMapper) {
        this.repository = Objects.requireNonNull(repository, "RefDismembermentRepository ne peut pas être null");
        this.repositoryMapper = Objects.requireNonNull(repositoryMapper,
                "RefDismembermentMapper ne peut pas être null");
    }

    public List<RefDismembermentDto> getByPropertyType(String typeProperty) {

        return repositoryMapper.toDTOList(repository.findByPropertyType(typeProperty));
    }
}
