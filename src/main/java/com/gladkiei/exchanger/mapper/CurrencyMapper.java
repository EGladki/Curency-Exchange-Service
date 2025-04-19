package com.gladkiei.exchanger.mapper;

import com.gladkiei.exchanger.dto.CurrencyResponseDTO;
import com.gladkiei.exchanger.models.Currency;

public class CurrencyMapper {

    public static CurrencyResponseDTO toDto(Currency currency) {
        return new CurrencyResponseDTO(
                currency.getId(),
                currency.getCode(),
                currency.getName(),
                currency.getSign());
    }
}
