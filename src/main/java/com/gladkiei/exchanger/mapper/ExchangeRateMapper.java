package com.gladkiei.exchanger.mapper;

import com.gladkiei.exchanger.dto.CurrencyResponseDTO;
import com.gladkiei.exchanger.dto.ExchangeRateResponseDTO;
import com.gladkiei.exchanger.models.ExchangeRate;

public class ExchangeRateMapper {
    public static ExchangeRateResponseDTO toDto(ExchangeRate exchangeRate) {
        CurrencyResponseDTO baseCurrencyDTO = new CurrencyResponseDTO(
                exchangeRate.getBaseCurrency().getId(),
                exchangeRate.getBaseCurrency().getCode(),
                exchangeRate.getBaseCurrency().getName(),
                exchangeRate.getBaseCurrency().getSign());

        CurrencyResponseDTO targetCurrencyDTO = new CurrencyResponseDTO(
                exchangeRate.getTargetCurrency().getId(),
                exchangeRate.getTargetCurrency().getCode(),
                exchangeRate.getTargetCurrency().getName(),
                exchangeRate.getTargetCurrency().getSign());

        return new ExchangeRateResponseDTO(
                exchangeRate.getId(),
                baseCurrencyDTO,
                targetCurrencyDTO,
                exchangeRate.getRate());
    }
}
