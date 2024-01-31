package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Amount;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AmountDAO implements DAOInterface<Amount>{
    private DatabaseConnection connection;

    public AmountDAO() {
        this.connection = new DatabaseConnection();
    }

    @Override
    public List<Amount> findAll() {
        List<Amount> amountList = new ArrayList<>();

        String sql= "SELECT * FROM amount";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                amountList.add(new Amount(
                        resultSet.getInt("account_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime()
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amountList;
    }

    @Override
    public Amount save(Amount toSave) {

        String sql = "INSERT INTO amount (account_id, amount, datetime) VALUES (?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, toSave.getAccountId());
            preparedStatement.setDouble(2, toSave.getAmount());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

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
    public List<Amount> saveAll(List<Amount> toSave) {
        List<Amount> amountList = new ArrayList<>();

        for (Amount amount : toSave) {
            Amount saveAmount = save(amount);
            if (saveAmount != null) {
                amountList.add(saveAmount);
            }
        }
        return amountList;
    }
}
