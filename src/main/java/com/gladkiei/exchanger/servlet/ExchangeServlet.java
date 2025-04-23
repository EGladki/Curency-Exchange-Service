package com.gladkiei.exchanger.servlet;

import com.gladkiei.exchanger.dto.ExchangeRequestDTO;
import com.gladkiei.exchanger.dto.ExchangeResponseDTO;
import com.gladkiei.exchanger.service.ExchangeService;
import com.gladkiei.exchanger.utils.JsonUtil;
import com.gladkiei.exchanger.utils.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ValidationUtil validationUtil = new ValidationUtil();
    private final ExchangeService exchangeService = new ExchangeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        validationUtil.codeValidation(baseCurrencyCode);
        validationUtil.codeValidation(targetCurrencyCode);
        validationUtil.amountValidation(amount);

        ExchangeRequestDTO requestDTO = new ExchangeRequestDTO(baseCurrencyCode, targetCurrencyCode, new BigDecimal(amount));
        ExchangeResponseDTO responseDTO = exchangeService.calculateExchangeRate(requestDTO);

        resp.setStatus(SC_OK);
        JsonUtil.sendJson(resp, responseDTO);
    }
}
