package com.gladkiei.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ExchangeRequestDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;

    public ExchangeRequestDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.amount = amount;
    }
}
