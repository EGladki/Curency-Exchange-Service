package com.gladkiei.exchanger.utils;

import com.gladkiei.exchanger.dto.CurrencyRequestDTO;
import com.gladkiei.exchanger.exception.BadRequestException;

import java.util.Currency;

public class ValidationUtil {

    public void validation(CurrencyRequestDTO currencyRequestDTO) {
        String code = currencyRequestDTO.getCode();
        String name = currencyRequestDTO.getName();
        String sign = currencyRequestDTO.getSign();

        if (isNullOrBlank(code)) {
            throw new BadRequestException("Missing parameter - code");
        }

        if (isNullOrBlank(name)) {
            throw new BadRequestException("Missing parameter - name");
        }

        if (isNullOrBlank(sign)) {
            throw new BadRequestException("Missing parameter - sign");
        }

        codeValidation(code);
        nameValidation(name);
        signValidation(sign);
    }

    public void validation(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        if (isNullOrBlank(baseCurrencyCode)) {
            throw new BadRequestException("Missing parameter - baseCurrencyCode");
        }

        if (isNullOrBlank(targetCurrencyCode)) {
            throw new BadRequestException("Missing parameter - targetCurrencyCode");
        }

        if (isNullOrBlank(rate)) {
            throw new BadRequestException("Missing parameter - rate");
        }

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new BadRequestException("Exchange rate must contain two different currencies");
        }

        codeValidation(baseCurrencyCode);
        codeValidation(targetCurrencyCode);
        rateValidation(rate);
    }

    public void validationCurrencyPair(String currencyPair) {
        if (isNullOrBlank(currencyPair)) {
            throw new BadRequestException("Missing parameters, currency codes (example: USDEUR)");
        }

        if (currencyPair.length() != 6) {
            throw new BadRequestException("Request must be exactly 6 Latin letters");
        }

    }

    public boolean isNullOrBlank(String string) {
        return (string == null || string.isBlank());
    }


    public void codeValidation(String code) {
        if (code.length() != 3 || !code.matches("[a-z,A-Z]{3}")) {
            throw new BadRequestException("Currency code must be exactly 3 Latin letters");
        }
        try {
            Currency.getInstance(code);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Currency code is not a valid ISO 4217 code");
        }
    }

    public void nameValidation(String name) {
        if (name.length() > 25) {
            throw new BadRequestException("Name length too large");
        }
        if (!name.matches("^[a-zA-Z-' ]+$")){
            throw new BadRequestException("Name must contain Latin letters only");
        }
    }

    public void signValidation(String sign) {
        if (sign.length() > 3) {
            throw new BadRequestException("Sign must be must be 1-3 symbols");
        }
    }

    public void rateValidation(String rateString) {
        double rate;
        try {
            rate = Double.parseDouble(rateString);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Exchange rate must be a number");
        }

        if (rate <= 0) {
            throw new BadRequestException("Exchange rate must be greater than 0");
        }

        if (rate > 1000000) {
            throw new BadRequestException("Exchange rate too large");
        }

        int decimalPointIndex = rateString.indexOf(".");
        if (decimalPointIndex != -1 && rateString.length() - decimalPointIndex - 1 > 6) {
            throw new BadRequestException("Exchange rate must not have more than 6 decimal places");
        }
    }

    public void amountValidation(String amountString) {
        double amount;
        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Amount must be a number");
        }

        if (amount <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }

        if (amount > 1000000000) {
            throw new BadRequestException("Amount too large");
        }

        int decimalPointIndex = amountString.indexOf(".");
        if (decimalPointIndex != -1 && amountString.length() - decimalPointIndex - 1 > 2) {
            throw new BadRequestException("Amount must not have more than 2 decimal places");
        }
    }
}
