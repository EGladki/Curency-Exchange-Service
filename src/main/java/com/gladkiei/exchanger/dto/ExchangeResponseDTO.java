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
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeResponseDTO(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, BigDecimal amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        convertedAmount = rate.multiply(amount).setScale(2,RoundingMode.HALF_UP);
    }
}
