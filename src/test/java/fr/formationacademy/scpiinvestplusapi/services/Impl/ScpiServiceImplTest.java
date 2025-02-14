package fr.formationacademy.scpiinvestplusapi.services.Impl;

import fr.formationacademy.scpiinvestplusapi.dto.ScpiDTO;
import fr.formationacademy.scpiinvestplusapi.mapper.ScpiMapper;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.service.ScpiService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ScpiServiceImplTest {

    @Mock
    private ScpiRepository scpiRepository;

    @Mock
    private ScpiMapper scpiMapper;

    @InjectMocks
    private ScpiService scpiServiceImpl;

     ScpiDTO ScpiServiceImplTest(Integer id){
        return scpiRepository.findById(id).map(scpiMapper::toDTO).orElse(null);
    }
}
