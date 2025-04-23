package com.gladkiei.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ExchangeRateResponseDTO {
    private int id;
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private BigDecimal rate;

    public ExchangeRateResponseDTO(int id, CurrencyResponseDTO baseCurrency, CurrencyResponseDTO targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
