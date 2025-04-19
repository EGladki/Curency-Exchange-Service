package com.gladkiei.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeRateResponseDTO {
    private int id;
    private CurrencyResponseDTO baseCurrency;
    private CurrencyResponseDTO targetCurrency;
    private double rate;

    public ExchangeRateResponseDTO(int id, CurrencyResponseDTO baseCurrency, CurrencyResponseDTO targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
