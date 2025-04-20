package com.gladkiei.exchanger.dao;

import com.gladkiei.exchanger.dto.CurrencyRequestDTO;
import com.gladkiei.exchanger.exception.AlreadyExistException;
import com.gladkiei.exchanger.exception.DatabaseAccessException;
import com.gladkiei.exchanger.models.Currency;
import com.gladkiei.exchanger.utils.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO {

    public List<Currency> getAll() {
        final String sql = "SELECT * FROM currencies";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);

             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }
            return currencies;

        } catch (SQLException e) {
            throw new DatabaseAccessException("Error access to database");
        }
    }

    public Optional<Currency> get(String code) {
        final String sql = "SELECT * FROM currencies WHERE UPPER(code) = UPPER(?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Currency currency = getCurrency(resultSet);
                return Optional.of(currency);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseAccessException("Error access to database");
        }
    }

    public Optional<Currency> insert(CurrencyRequestDTO currencyRequestDTO) {
        final String sql = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";
        String code = currencyRequestDTO.getCode();
        existValidation(code);

        String name = currencyRequestDTO.getName();
        String sign = currencyRequestDTO.getSign();

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, currencyRequestDTO.getCode());
            preparedStatement.setString(2, currencyRequestDTO.getName());
            preparedStatement.setString(3, currencyRequestDTO.getSign());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);

                    return Optional.of(new Currency(id, code, name, sign));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Error access to database");
        }
        return Optional.empty();
    }

    protected Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }

    private void existValidation(String code) {
        Optional<Currency> currencyOptional = get(code);
        if (currencyOptional.isPresent()) {
            throw new AlreadyExistException("Such currency already exist");
        }
    }
}

