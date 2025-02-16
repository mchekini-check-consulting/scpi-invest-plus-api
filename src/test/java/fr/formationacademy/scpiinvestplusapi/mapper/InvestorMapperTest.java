package fr.formationacademy.scpiinvestplusapi.mapper;



import fr.formationacademy.scpiinvestplusapi.dto.InvestorDTO;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvestorMapperTest {

    private InvestorMapper investorMapper;
    private Investor investor;
    private InvestorDTO investorDTO;

    @BeforeEach
    void setUp() {

        investorMapper = Mappers.getMapper(InvestorMapper.class);


        investor = new Investor();
        investor.setEmail("test@example.com");
        investor.setFirstName("John");
        investor.setLastName("Doe");
        investor.setDateOfBirth(LocalDate.of(1980, 1, 1));
        investor.setAnnualIncome(50000);
        investor.setPhoneNumber("1234567890");
        investor.setMaritalStatus("Single");
        investor.setNumberOfChildren("0");


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
    void testToDTO() {

        InvestorDTO resultDTO = investorMapper.toDTO(investor);


        assertNotNull(resultDTO);
        assertEquals(investor.getEmail(), resultDTO.getEmail());
        assertEquals(investor.getFirstName(), resultDTO.getFirstName());
        assertEquals(investor.getLastName(), resultDTO.getLastName());
        assertEquals(investor.getDateOfBirth(), resultDTO.getDateOfBirth());
        assertEquals(investor.getAnnualIncome(), resultDTO.getAnnualIncome());
        assertEquals(investor.getPhoneNumber(), resultDTO.getPhoneNumber());
        assertEquals(investor.getMaritalStatus(), resultDTO.getMaritalStatus());
        assertEquals(investor.getNumberOfChildren(), resultDTO.getNumberOfChildren());
    }

    @Test
    void testToEntity() {

        Investor resultEntity = investorMapper.toEntity(investorDTO);


        assertNotNull(resultEntity);
        assertEquals(investorDTO.getEmail(), resultEntity.getEmail());
        assertEquals(investorDTO.getFirstName(), resultEntity.getFirstName());
        assertEquals(investorDTO.getLastName(), resultEntity.getLastName());
        assertEquals(investorDTO.getDateOfBirth(), resultEntity.getDateOfBirth());
        assertEquals(investorDTO.getAnnualIncome(), resultEntity.getAnnualIncome());
        assertEquals(investorDTO.getPhoneNumber(), resultEntity.getPhoneNumber());
        assertEquals(investorDTO.getMaritalStatus(), resultEntity.getMaritalStatus());
        assertEquals(investorDTO.getNumberOfChildren(), resultEntity.getNumberOfChildren());
    }
}