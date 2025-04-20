package com.gladkiei.exchanger.servlet;

import com.gladkiei.exchanger.dao.CurrencyDAO;
import com.gladkiei.exchanger.dto.CurrencyResponseDTO;
import com.gladkiei.exchanger.exception.NotFoundException;
import com.gladkiei.exchanger.mapper.CurrencyMapper;
import com.gladkiei.exchanger.models.Currency;
import com.gladkiei.exchanger.utils.JsonUtil;
import com.gladkiei.exchanger.utils.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final ValidationUtil validationUtil = new ValidationUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getPathInfo().substring(1).toUpperCase();

        validationUtil.codeValidation(code);

        Optional<Currency> currencyOptional = currencyDAO.get(code);
        if (currencyOptional.isEmpty()) {
            throw new NotFoundException("Currency with code " + code + " not found");
        }

        CurrencyResponseDTO currencyDTO = CurrencyMapper.toDto(currencyOptional.get());

        resp.setStatus(HttpServletResponse.SC_OK);
        JsonUtil.sendJson(resp, currencyDTO);
    }
}
