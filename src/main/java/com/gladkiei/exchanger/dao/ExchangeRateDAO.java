package com.gladkiei.exchanger.dao;

import com.gladkiei.exchanger.dto.ExchangeRateRequestDTO;
import com.gladkiei.exchanger.exception.AlreadyExistException;
import com.gladkiei.exchanger.exception.BadRequestException;
import com.gladkiei.exchanger.exception.DatabaseAccessException;
import com.gladkiei.exchanger.models.Currency;
import com.gladkiei.exchanger.models.ExchangeRate;
import com.gladkiei.exchanger.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    public List<ExchangeRate> getAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        final String sql = """
                SELECT er.id,
                       er.rate,
                       bc.id        as base_id,
                       bc.code      as base_code,
                       bc.full_name as base_name,
                       bc.sign      as base_sign,
                       tc.id        as target_id,
                       tc.code      as target_code,
                       tc.full_name as target_name,
                       tc.sign      as target_sign
                
                FROM exchange_rates er
                         JOIN currencies bc on bc.id = er.base_currency_id
                         JOIN currencies tc on tc.id = er.target_currency_id
                """;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ExchangeRate rate = new ExchangeRate(
                        resultSet.getInt("id"),
                        getBaseCurrency(resultSet),
                        getTargetCurrency(resultSet),
                        resultSet.getDouble("rate")
                );
                exchangeRates.add(rate);
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException("Error access to database");
        }
        return exchangeRates;
    }

    public Optional<ExchangeRate> get(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        final String sql = """
                SELECT er.id,
                       er.rate,
                       bc.id        as base_id,
                       bc.code      as base_code,
                       bc.full_name as base_name,
                       bc.sign      as base_sign,
                       tc.id        as target_id,
                       tc.code      as target_code,
                       tc.full_name as target_name,
                       tc.sign      as target_sign
                
                FROM exchange_rates er
                         JOIN currencies bc on bc.id = er.base_currency_id
                         JOIN currencies tc on tc.id = er.target_currency_id
                WHERE bc.code = UPPER(?)
                  AND tc.code = UPPER(?)
                """;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, exchangeRateRequestDTO.getBaseCurrencyCode());
            statement.setString(2, exchangeRateRequestDTO.getTargetCurrencyCode());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new ExchangeRate(
                        resultSet.getInt("id"),
                        getBaseCurrency(resultSet),
                        getTargetCurrency(resultSet),
                        resultSet.getDouble("rate")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Error access to database");
        }
        return Optional.empty();
    }

    public Optional<ExchangeRate> insert(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        existValidation(exchangeRateRequestDTO);

        final String sql = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";

        String baseCurrencyCode = exchangeRateRequestDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDTO.getTargetCurrencyCode();
        double rate = exchangeRateRequestDTO.getRate();

        int baseCurrencyId = getCurrencyIdByCode(baseCurrencyCode);
        int targetCurrencyId = getCurrencyIdByCode(targetCurrencyCode);

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            preparedStatement.setDouble(3, rate);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new BadRequestException("Failed to insert exchange rate");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);

                Currency baseCurrency = getCurrencyById(baseCurrencyId);
                Currency targetCurrency = getCurrencyById(targetCurrencyId);

                return Optional.of(new ExchangeRate(id, baseCurrency, targetCurrency, rate));
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException("Error access to database");
        }
        return Optional.empty();
    }

    public Optional<ExchangeRate> update(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        final String sql = """
                UPDATE exchange_rates
                SET rate = (?)
                WHERE base_currency_id = (SELECT currencies.id FROM currencies WHERE code = UPPER(?))
                    AND
                    target_currency_id = (SELECT currencies.id FROM currencies WHERE code = UPPER(?));
                """;
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, exchangeRateRequestDTO.getRate());
            preparedStatement.setString(2, exchangeRateRequestDTO.getBaseCurrencyCode());
            preparedStatement.setString(3, exchangeRateRequestDTO.getTargetCurrencyCode());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new BadRequestException("Exchange rate not found or not updated");
            }

            return get(new ExchangeRateRequestDTO(
                    exchangeRateRequestDTO.getBaseCurrencyCode(),
                    exchangeRateRequestDTO.getTargetCurrencyCode()));

        } catch (SQLException e) {
            throw new DatabaseAccessException("Error access to database");
        }
    }

    public Integer getCurrencyIdByCode(String code) {
        final String sql = "SELECT id FROM currencies WHERE code = UPPER (?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException e) {
            throw new BadRequestException("Failed getting currency ID from database");
        }
        return null;
    }

    public Currency getCurrencyById(int id) {
        final String sql = "SELECT * FROM currencies WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return currencyDAO.getCurrency(resultSet);
            }
        } catch (SQLException e) {
            throw new BadRequestException("Failed getting currency by ID from database");
        }
        return null;
    }

    private Currency getBaseCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("base_id"),
                resultSet.getString("base_code"),
                resultSet.getString("base_name"),
                resultSet.getString("base_sign"));
    }

    private Currency getTargetCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("target_id"),
                resultSet.getString("target_code"),
                resultSet.getString("target_name"),
                resultSet.getString("target_sign"));
    }

    private void existValidation(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        Optional<ExchangeRate> exchangeRate = get(exchangeRateRequestDTO);
        if (exchangeRate.isPresent()) {
            throw new AlreadyExistException("Such exchange rate already exist");
        }
    }
}
