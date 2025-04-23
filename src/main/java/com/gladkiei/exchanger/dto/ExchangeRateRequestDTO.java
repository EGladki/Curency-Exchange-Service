package com.gladkiei.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ExchangeRateRequestDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;

    public ExchangeRateRequestDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public ExchangeRateRequestDTO(String baseCurrencyCode, String targetCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
    }
}
