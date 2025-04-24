package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class InvestmentStateDtoOut {
    public double totalInvesti;
    public Page<InvestmentDtoOut> investments;
}
