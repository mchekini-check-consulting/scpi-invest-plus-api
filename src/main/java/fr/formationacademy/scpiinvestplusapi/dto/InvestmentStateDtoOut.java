package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class InvestmentStateDtoOut {
    public int totalInvesti;
    public Page<InvestmentDtoOut> investments;
}
