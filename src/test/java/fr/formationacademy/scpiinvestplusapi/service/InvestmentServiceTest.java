package fr.formationacademy.scpiinvestplusapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import fr.formationacademy.scpiinvestplusapi.dto.InvestmentDto;
import fr.formationacademy.scpiinvestplusapi.entity.Investment;
import fr.formationacademy.scpiinvestplusapi.entity.Investor;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.mapper.InvestmentMapper;
import fr.formationacademy.scpiinvestplusapi.repository.InvestmentRepository;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private ScpiRepository scpiRepository;

    @Mock
    private InvestmentMapper investmentMapper;

    @Mock
    private InvestorService investorService;

    @InjectMocks
    private InvestmentService investmentService;

    private Investment investment;
    private InvestmentDto investmentDto;
    private Investor investor;
    private Scpi scpi;

    @BeforeEach
    void setUp() {
        investor = new Investor();
        investor.setEmail("investor@example.com");

        scpi = new Scpi();
        scpi.setId(1);
        scpi.setName("SCPI Test");

        investment = new Investment();
        investment.setNumberShares(10000);
        investment.setNumberYears(5);
        investment.setTotalAmount(new BigDecimal("5000"));
        investment.setInvestor(investor);
        investment.setScpi(scpi);

        investmentDto = new InvestmentDto();
        investmentDto.setNumberShares(10000);
        investmentDto.setNumberYears(5);
        investmentDto.setTotalAmount(new BigDecimal("5000"));
        investmentDto.setScpiId(1);
    }

    @Test
    void shouldSaveInvestmentSuccessfully() {
        // GIVEN
        when(investmentMapper.toEntity(investmentDto)).thenReturn(investment);
        when(investorService.getCurrentInvestor()).thenReturn(investor);
        when(investmentRepository.save(investment)).thenReturn(investment);
        when(investmentMapper.toDTO(investment)).thenReturn(investmentDto);

        // WHEN
        InvestmentDto result = investmentService.saveInvestment(investmentDto);

        // THEN
        assertThat(result).isNotNull();
        verify(investmentRepository).save(investment);
    }

}
