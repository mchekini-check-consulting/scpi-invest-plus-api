package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestorServiceTest {

    @Mock
    private InvestorRepository investorRepository;
    @Mock
    private InvestorMapper investorMapper;

    @InjectMocks
    private InvestorService investorService;

    private InvestorDTO investorDTO;

    @BeforeEach
    void setUp() {
        investorDTO = new InvestorDTO();
        investorDTO.setEmail("test@example.com");
        investorDTO.setFirstName("John");
        investorDTO.setLastName("Doe");
        investorDTO.setDateOfBirth(LocalDate.of(1980, 1, 1));
        investorDTO.setAnnualIncome(50000);
        investorDTO.setPhoneNumber("1234567890");
        investorDTO.setMaritalStatus("Single");
        investorDTO.setNumberOfChildren("0");
    }


    @Test
    void testCreateOrUpdateInvestor_WhenInvestorExists() {
        // Arrange
        String email = "test@example.com";
        Investor existingInvestor = new Investor();
        existingInvestor.setEmail(email);
        existingInvestor.setFirstName("OldFirstName");
        existingInvestor.setLastName("OldLastName");

        Investor updatedInvestor = new Investor();
        updatedInvestor.setEmail(email);
        updatedInvestor.setFirstName("NewFirstName");
        updatedInvestor.setLastName("NewLastName");

        when(investorRepository.findById(email)).thenReturn(Optional.of(existingInvestor));
        when(investorMapper.toEntity(investorDTO)).thenReturn(updatedInvestor);
        when(investorRepository.save(updatedInvestor)).thenReturn(updatedInvestor);

        // Act
        Investor result = investorService.createOrUpdateInvestor(email, investorDTO);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("NewFirstName", result.getFirstName());
        assertEquals("NewLastName", result.getLastName());

        verify(investorRepository, times(1)).findById(email);
        verify(investorMapper, times(1)).toEntity(investorDTO);
        verify(investorRepository, times(1)).save(updatedInvestor);
    }

    @Test
    void testCreateOrUpdateInvestor_WhenInvestorDoesNotExist() {
        String email = "new@example.com";
        Investor newInvestor = new Investor();
        newInvestor.setEmail(email);
        newInvestor.setFirstName("John");
        newInvestor.setLastName("Doe");

        when(investorRepository.findById(email)).thenReturn(Optional.empty());
        when(investorMapper.toEntity(investorDTO)).thenReturn(newInvestor);
        when(investorRepository.save(newInvestor)).thenReturn(newInvestor);

        Investor result = investorService.createOrUpdateInvestor(email, investorDTO);


        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(investorRepository, times(1)).findById(email);
        verify(investorMapper, times(1)).toEntity(investorDTO);
        verify(investorRepository, times(1)).save(newInvestor);
    }
    @Test
    void testGetAllInvestors() {
        Investor investor1 = new Investor();
        investor1.setEmail("investor1@example.com");

        Investor investor2 = new Investor();
        investor2.setEmail("investor2@example.com");

        List<Investor> investors = List.of(investor1, investor2);

        InvestorDTO investorDTO1 = new InvestorDTO();

        investorDTO1.setEmail("investor1@example.com");

        InvestorDTO investorDTO2 = new InvestorDTO();
        investorDTO2.setEmail("investor2@example.com");

        List<InvestorDTO> expectedDTOs = List.of(investorDTO1, investorDTO2);


        when(investorRepository.findAll()).thenReturn(investors);
        when(investorMapper.toDTO(investor1)).thenReturn(investorDTO1);
        when(investorMapper.toDTO(investor2)).thenReturn(investorDTO2);


        List<InvestorDTO> result = investorService.getAllInvestors();


        verify(investorRepository, times(1)).findAll();


        verify(investorMapper, times(1)).toDTO(investor1);
        verify(investorMapper, times(1)).toDTO(investor2);


        assertEquals(expectedDTOs, result);
    }

    @Test
    void testGetInvestorByEmail_WhenInvestorExists() {

        String email = "test@example.com";
        Investor investor = new Investor();
        investor.setEmail(email);
        investor.setFirstName("John");
        investor.setLastName("Doe");


        when(investorRepository.findById(email)).thenReturn(Optional.of(investor));


        Optional<Investor> result = investorService.getInvestorByEmail(email);


        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());


        verify(investorRepository, times(1)).findById(email);
    }

    @Test
    void testGetInvestorByEmail_WhenInvestorDoesNotExist() {
        String email = "nonexistent@example.com";
        when(investorRepository.findById(email)).thenReturn(Optional.empty());
        Optional<Investor> result = investorService.getInvestorByEmail(email);
        assertFalse(result.isPresent());
        verify(investorRepository, times(1)).findById(email);
    }

}