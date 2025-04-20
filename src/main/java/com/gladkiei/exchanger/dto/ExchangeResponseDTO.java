package com.gladkiei.exchanger.dto;

import com.gladkiei.exchanger.models.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
public class ExchangeResponseDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private BigDecimal convertedAmount;

    public ExchangeResponseDTO(Currency baseCurrency, Currency targetCurrency, double rate, double amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        convertedAmount = BigDecimal.valueOf(rate * amount).setScale(2, RoundingMode.DOWN);
    }
}
