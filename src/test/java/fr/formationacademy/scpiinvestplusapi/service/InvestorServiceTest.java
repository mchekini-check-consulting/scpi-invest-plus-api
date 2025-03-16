package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestorMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
    void createInvestor_ShouldCreateInvestor_WhenEmailIsUnique() throws GlobalException {
        // Given
        InvestorDTO investorDTO = new InvestorDTO();
        investorDTO.setEmail("test@example.com");

        Investor investor = new Investor();
        investor.setEmail("test@example.com");


        when(investorMapper.toEntity(investorDTO)).thenReturn(investor);
        when(investorRepository.existsById(investor.getEmail())).thenReturn(false);
        when(investorRepository.save(any(Investor.class))).thenReturn(investor);


        Investor result = investorService.createInvestor(investorDTO);


        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(investorRepository).save(investor);
    }

    @Test
    void createInvestor_ShouldThrowException_WhenEmailAlreadyExists() {

        InvestorDTO investorDTO = new InvestorDTO();
        investorDTO.setEmail("existing@example.com");

        Investor investor = new Investor();
        investor.setEmail("existing@example.com");


        when(investorMapper.toEntity(investorDTO)).thenReturn(investor);
        when(investorRepository.existsById(investor.getEmail())).thenReturn(true);


        GlobalException thrown = assertThrows(GlobalException.class, () -> {
            investorService.createInvestor(investorDTO);
        });

        assertEquals(HttpStatus.CONFLICT, thrown.getHttpStatus());
        assertEquals("An investor with this email already exists.", thrown.getMessage());
        verify(investorRepository, never()).save(any(Investor.class));
    }

    @Test
    void updateInvestor_ShouldUpdateInvestor_WhenEmailExists() throws GlobalException {
        // Given
        String email = "test@example.com";

        Investor existingInvestor = new Investor();
        existingInvestor.setEmail(email);

        Investor updatedInvestor = new Investor();
        updatedInvestor.setEmail(email);
        updatedInvestor.setFirstName("Updated Name");

        when(investorRepository.findById(email)).thenReturn(Optional.of(existingInvestor));
        when(investorMapper.toEntity(investorDTO)).thenReturn(updatedInvestor);
        when(investorRepository.save(updatedInvestor)).thenReturn(updatedInvestor);

        // When
        Investor result = investorService.updateInvestor(email, investorDTO);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Updated Name", result.getFirstName());

        verify(investorRepository).save(updatedInvestor);
    }


    @Test
    void updateInvestor_ShouldThrowException_WhenEmailDoesNotExist() {
        // Given
        String email = "nonexistent@example.com";
        when(investorRepository.findById(email)).thenReturn(Optional.empty());

        // When & Then
        GlobalException thrown = assertThrows(GlobalException.class, () -> {
            investorService.updateInvestor(email, investorDTO);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
        assertEquals("Investor not found with email: " + email, thrown.getMessage());
        verify(investorRepository, never()).save(any(Investor.class));
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