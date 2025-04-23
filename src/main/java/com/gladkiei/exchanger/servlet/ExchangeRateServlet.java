package com.gladkiei.exchanger.servlet;

import com.gladkiei.exchanger.dao.ExchangeRateDAO;
import com.gladkiei.exchanger.dto.ExchangeRateRequestDTO;
import com.gladkiei.exchanger.dto.ExchangeRateResponseDTO;
import com.gladkiei.exchanger.exception.NotFoundException;
import com.gladkiei.exchanger.mapper.ExchangeRateMapper;
import com.gladkiei.exchanger.models.ExchangeRate;
import com.gladkiei.exchanger.utils.JsonUtil;
import com.gladkiei.exchanger.utils.ParserUtil;
import com.gladkiei.exchanger.utils.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private final ValidationUtil validationUtil = new ValidationUtil();
    private final ParserUtil parserUtil = new ParserUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyPair = req.getPathInfo().substring(1).toUpperCase();
        validationUtil.validationCurrencyPair(currencyPair);

        String baseCode = currencyPair.substring(0, 3);
        validationUtil.codeValidation(baseCode);

        String targetCode = currencyPair.substring(3, 6);
        validationUtil.codeValidation(targetCode);

        ExchangeRateRequestDTO requestDTO = new ExchangeRateRequestDTO(baseCode, targetCode);
        Optional<ExchangeRate> exchangeRate = exchangeRateDAO.get(requestDTO);
        if (exchangeRate.isEmpty()) {
            throw new NotFoundException("Exchange rate not found");
        }

        ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateMapper.toDto(exchangeRate.get());
        resp.setStatus(SC_OK);
        JsonUtil.sendJson(resp, exchangeRateDTO);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyPair = req.getPathInfo().substring(1).toUpperCase();
        validationUtil.validationCurrencyPair(currencyPair);

        String baseCode = currencyPair.substring(0, 3);
        validationUtil.codeValidation(baseCode);

        String targetCode = currencyPair.substring(3, 6);
        validationUtil.codeValidation(targetCode);

        String rateString = parserUtil.extractRateFromRequest(req);
        validationUtil.rateValidation(rateString);

        BigDecimal rate = new BigDecimal(rateString);
        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCode, targetCode, rate);

        Optional<ExchangeRate> exchangeRate = exchangeRateDAO.update(exchangeRateRequestDTO);

        if (exchangeRate.isEmpty()) {
            throw new NotFoundException("Exchange rate " + currencyPair + " not found");
        }

        ExchangeRateResponseDTO exchangeRateDTO = ExchangeRateMapper.toDto(exchangeRate.get());
        resp.setStatus(SC_OK);
        JsonUtil.sendJson(resp, exchangeRateDTO);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, res);
            return;
        }
        this.doPatch(req, res);
    }
}
