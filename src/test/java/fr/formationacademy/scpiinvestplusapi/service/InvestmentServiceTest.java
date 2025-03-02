package fr.formationacademy.scpiinvestplusapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import java.util.List;
import java.util.Optional;

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
        investment.setNumberShares(new BigDecimal("10000.50"));
        investment.setNumberYears(5);
        investment.setTotalAmount(new BigDecimal("5000"));
        investment.setInvestmentState("En cours");
        investment.setInvestor(investor);
        investment.setScpi(scpi);

        investmentDto = new InvestmentDto();
        investmentDto.setNumberShares(new BigDecimal("10000.50"));
        investmentDto.setNumberYears(5);
        investmentDto.setInvestmentState("En cours");
        investmentDto.setTotalAmount(new BigDecimal("5000"));
        investmentDto.setScpiId(1);
    }

    @Test
    void shouldReturnAllInvestments() {
        // GIVEN
        when(investmentRepository.findAll()).thenReturn(List.of(investment));
        when(investmentMapper.toDTO(investment)).thenReturn(investmentDto);

        // WHEN
        List<InvestmentDto> result = investmentService.getAllInvestments();

        // THEN
        assertThat(result).isNotNull().hasSize(1);
        verify(investmentRepository).findAll();
        verify(investmentMapper).toDTO(investment);
    }

    @Test
    void shouldReturnInvestmentsByInvestorEmail() {
        // GIVEN
        String email = "investor@example.com";
        when(investmentRepository.findByInvestorEmail(email)).thenReturn(List.of(investment));

        // WHEN
        List<Investment> result = investmentService.getInvestmentsByInvestorEmail(email);

        // THEN
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getInvestor().getEmail()).isEqualTo(email);
        verify(investmentRepository).findByInvestorEmail(email);
    }

    @Test
    void shouldReturnInvestmentsByScpiId() {
        // GIVEN
        when(investmentRepository.findByScpiId(1)).thenReturn(List.of(investment));

        // WHEN
        List<Investment> result = investmentService.getInvestmentsByScpiId(1);

        // THEN
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getScpi().getId()).isEqualTo(1);
        verify(investmentRepository).findByScpiId(1);
    }

    @Test
    void shouldSaveInvestmentSuccessfully() {

        when(investmentMapper.toEntity(investmentDto)).thenReturn(investment);
        when(investorService.getCurrentInvestor()).thenReturn(investor);
        when(investmentRepository.save(investment)).thenReturn(investment);
        when(investmentMapper.toDTO(investment)).thenReturn(investmentDto);

        InvestmentDto result = investmentService.saveInvestment(investmentDto);

        assertThat(result).isNotNull();
        verify(investmentRepository).save(investment);
    }

    @Test
    void shouldThrowExceptionWhenScpiNotFound() {
        // GIVEN
        when(investmentMapper.toEntity(investmentDto)).thenReturn(investment);
        when(investorService.getCurrentInvestor()).thenReturn(investor);
        when(scpiRepository.findById(1)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> investmentService.saveInvestment(investmentDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("SCPI non trouvée");

        verify(scpiRepository).findById(1);
        verify(investmentRepository, never()).save(any());
    }
}
