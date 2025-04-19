package com.gladkiei.exchanger.dto;

import com.gladkiei.exchanger.models.Currency;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeResponseDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ExchangeResponseDTO(Currency baseCurrency, Currency targetCurrency, double rate, double amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        convertedAmount = rate * amount;
    }
}
