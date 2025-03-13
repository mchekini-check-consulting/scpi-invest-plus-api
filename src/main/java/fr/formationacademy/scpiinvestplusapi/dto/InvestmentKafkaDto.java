package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

@Data
public class InvestmentKafkaDto {
    private InvestmentDto investmentDto;
    private String investorEmail;
    private ScpiDtoOut scpi;
}
