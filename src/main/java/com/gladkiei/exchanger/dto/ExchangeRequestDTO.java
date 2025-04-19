package com.gladkiei.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeRequestDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private double amount;

    public ExchangeRequestDTO(String baseCurrencyCode, String targetCurrencyCode, double amount) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.amount = amount;
    }
}
