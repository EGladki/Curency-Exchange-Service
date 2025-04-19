package com.gladkiei.exchanger.servlet;

import com.gladkiei.exchanger.dao.ExchangeRateDAO;
import com.gladkiei.exchanger.dto.ExchangeRateRequestDTO;
import com.gladkiei.exchanger.dto.ExchangeRateResponseDTO;
import com.gladkiei.exchanger.exception.DatabaseAccessException;
import com.gladkiei.exchanger.mapper.ExchangeRateMapper;
import com.gladkiei.exchanger.models.ExchangeRate;
import com.gladkiei.exchanger.utils.JsonUtil;
import com.gladkiei.exchanger.utils.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private final ValidationUtil validationUtil = new ValidationUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRate> exchangeRates = exchangeRateDAO.getAll();
        List<ExchangeRateResponseDTO> exchangeRateDTO = new ArrayList<>();

        for (ExchangeRate exchangeRate : exchangeRates) {
            ExchangeRateResponseDTO dto = ExchangeRateMapper.toDto(exchangeRate);
            exchangeRateDTO.add(dto);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtil.sendJson(resp, exchangeRateDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateString = req.getParameter("rate");

        validationUtil.validation(baseCurrencyCode, targetCurrencyCode, rateString);

        double rate = Double.parseDouble(rateString);

        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCurrencyCode, targetCurrencyCode, rate);
        Optional<ExchangeRate> exchangeRate = exchangeRateDAO.insert(exchangeRateRequestDTO);

        if (exchangeRate.isEmpty()) {
            throw new DatabaseAccessException("Error access to database");
        }

        ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateMapper.toDto(exchangeRate.get());

        resp.setStatus(HttpServletResponse.SC_CREATED);
        JsonUtil.sendJson(resp, exchangeRateDTO);
    }
}

