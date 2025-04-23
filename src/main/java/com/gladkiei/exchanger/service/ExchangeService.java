package com.gladkiei.exchanger.service;

import com.gladkiei.exchanger.dao.ExchangeRateDAO;
import com.gladkiei.exchanger.dto.ExchangeRateRequestDTO;
import com.gladkiei.exchanger.dto.ExchangeRequestDTO;
import com.gladkiei.exchanger.dto.ExchangeResponseDTO;
import com.gladkiei.exchanger.exception.NotFoundException;
import com.gladkiei.exchanger.models.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private final static String USD_CODE = "USD";
    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    public ExchangeResponseDTO calculateExchangeRate(ExchangeRequestDTO exchangeRequestDTO) {
        Optional<ExchangeResponseDTO> direct = getDirect(exchangeRequestDTO);
        if (direct.isPresent()) {
            return direct.get();
        }

        Optional<ExchangeResponseDTO> reversed = getReversed(exchangeRequestDTO);
        if (reversed.isPresent()) {
            return reversed.get();
        }

        Optional<ExchangeResponseDTO> cross = getCross(exchangeRequestDTO);
        if (cross.isPresent()) {
            return cross.get();
        }

        throw new NotFoundException("Exchange rate not found");
    }

    public Optional<ExchangeResponseDTO> getDirect(ExchangeRequestDTO exchangeRequestDTO) {
        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(exchangeRequestDTO.getBaseCurrencyCode(), exchangeRequestDTO.getTargetCurrencyCode());
        Optional<ExchangeRate> exchangeRate = exchangeRateDAO.get(exchangeRateRequestDTO);
        if (exchangeRate.isPresent()) {
            return Optional.of(new ExchangeResponseDTO(
                    exchangeRate.get().getBaseCurrency(),
                    exchangeRate.get().getTargetCurrency(),
                    exchangeRate.get().getRate(),
                    exchangeRequestDTO.getAmount()));
        }
        return Optional.empty();
    }

    public Optional<ExchangeResponseDTO> getReversed(ExchangeRequestDTO exchangeRequestDTO) {
        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(exchangeRequestDTO.getTargetCurrencyCode(), exchangeRequestDTO.getBaseCurrencyCode());
        Optional<ExchangeRate> exchangeRate = exchangeRateDAO.get(exchangeRateRequestDTO);
        if (exchangeRate.isPresent()) {
            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.get().getRate(),RoundingMode.HALF_UP);
            return Optional.of(new ExchangeResponseDTO(
                    exchangeRate.get().getTargetCurrency(),
                    exchangeRate.get().getBaseCurrency(),
                    rate,
                    exchangeRequestDTO.getAmount()));
        }
        return Optional.empty();
    }

    public Optional<ExchangeResponseDTO> getCross(ExchangeRequestDTO exchangeRequestDTO) {
        ExchangeRateRequestDTO usdBaseRequestDTO = new ExchangeRateRequestDTO(USD_CODE, exchangeRequestDTO.getBaseCurrencyCode());
        Optional<ExchangeRate> usdBaseExchangeRate = exchangeRateDAO.get(usdBaseRequestDTO);

        if (usdBaseExchangeRate.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal usdBaseRate = usdBaseExchangeRate.get().getRate();

        ExchangeRateRequestDTO usdTargetRequestDTO = new ExchangeRateRequestDTO(USD_CODE, exchangeRequestDTO.getTargetCurrencyCode());
        Optional<ExchangeRate> usdTargetExchangeRate = exchangeRateDAO.get(usdTargetRequestDTO);

        if (usdTargetExchangeRate.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal usdTargetRate = usdTargetExchangeRate.get().getRate();
        BigDecimal resultRate = usdTargetRate.divide(usdBaseRate, RoundingMode.HALF_UP);
        return Optional.of(new ExchangeResponseDTO(
                usdBaseExchangeRate.get().getTargetCurrency(),
                usdTargetExchangeRate.get().getTargetCurrency(),
                resultRate,
                exchangeRequestDTO.getAmount()));
    }
}
