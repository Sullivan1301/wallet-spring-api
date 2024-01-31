package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Amount;
import org.example.model.Category;
import org.example.model.Currency;
import org.example.model.CurrencyValue;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CurrencyValueDAO implements DAOInterface<CurrencyValue>{
    private DatabaseConnection connection;

    public CurrencyValueDAO() {
        this.connection = new DatabaseConnection();
    }

    @Override
    public List<CurrencyValue> findAll() {
        List<CurrencyValue> currencyValueList = new ArrayList<>();

        String sql= "SELECT * FROM currency_value";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                currencyValueList.add(new CurrencyValue(
                        resultSet.getInt("value_id"),
                        resultSet.getInt("value_id_source"),
                        resultSet.getInt("value_id_destination"),
                        resultSet.getDouble("value"),
                        resultSet.getTimestamp("effect_date").toLocalDateTime()
                ));
                for (CurrencyValue currencyValue : currencyValueList){
                    currencyValue.toString();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return currencyValueList;
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave) {

        String sql = "INSERT INTO currency_value (value_id_source, value_id_destination, value, effect_date) VALUES (?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, toSave.getCurrencyIdSource());
            preparedStatement.setInt(2, toSave.getCurrencyIdDestination());
            preparedStatement.setDouble(3, toSave.getValue());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            int rowAdded = preparedStatement.executeUpdate();
            if (rowAdded > 0) {
                return toSave;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave) {
        List<CurrencyValue> currencyValueList = new ArrayList<>();

        for (CurrencyValue currencyValue : toSave) {
            CurrencyValue saveCurrencyValue = save(currencyValue);
            if (saveCurrencyValue != null) {
                currencyValueList.add(saveCurrencyValue);
            }
        }
        return currencyValueList;
    }
    public double currentCurrencyValue(){
        String sql= " select * from currency_value order by value_id DESC LIMIT 1; " ;

        double value = 0.0;
        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                value= resultSet.getDouble("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }
}
