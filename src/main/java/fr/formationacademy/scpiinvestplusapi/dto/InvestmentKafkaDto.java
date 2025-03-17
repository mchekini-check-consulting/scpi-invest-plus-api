package fr.formationacademy.scpiinvestplusapi.dto;

import lombok.Data;

@Data
public class InvestmentKafkaDto {
    private InvestmentOutDto investmentDto;
    private String investorEmail;
    private ScpiDtoOut scpi;
}
