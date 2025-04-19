package com.gladkiei.exchanger.servlet;

import com.gladkiei.exchanger.dao.CurrencyDAO;
import com.gladkiei.exchanger.dto.CurrencyRequestDTO;
import com.gladkiei.exchanger.dto.CurrencyResponseDTO;
import com.gladkiei.exchanger.exception.DatabaseAccessException;
import com.gladkiei.exchanger.mapper.CurrencyMapper;
import com.gladkiei.exchanger.models.Currency;
import com.gladkiei.exchanger.utils.JsonUtil;
import com.gladkiei.exchanger.utils.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    private final ValidationUtil validationUtil = new ValidationUtil();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Currency> currencies = currencyDAO.getAll();
        List<CurrencyResponseDTO> currencyDTO = new ArrayList<>();

        for (Currency currency : currencies) {
            CurrencyResponseDTO dto = CurrencyMapper.toDto(currency);
            currencyDTO.add(dto);
        }

        resp.setStatus(SC_OK);
        JsonUtil.sendJson(resp, currencyDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(code, name, sign);
        validationUtil.validation(currencyRequestDTO);

        Optional<Currency> currency = currencyDAO.insert(currencyRequestDTO);

        if (currency.isEmpty()) {
            throw new DatabaseAccessException("Error adding currency to database");
        }

        CurrencyResponseDTO currencyDTO = CurrencyMapper.toDto(currency.get());

        resp.setStatus(SC_CREATED);
        JsonUtil.sendJson(resp, currencyDTO);
    }
}
