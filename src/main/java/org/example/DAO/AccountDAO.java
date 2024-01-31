package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;
import org.example.model.Amount;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements DAOInterface<Account>{
    private DatabaseConnection connection;
    private AmountDAO amountDAO;

    public AccountDAO() {
        this.connection = new DatabaseConnection();
        this.amountDAO = new AmountDAO();
    }

    @Override
    public List<Account> findAll() {
        List<Account> accountList = new ArrayList<>();


        String sql = "SELECT * FROM account;";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {

                accountList.add(new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("account_name"),
                        resultSet.getInt("account_currency"),
                        resultSet.getString("account_type")
                ));
                for (Account account : accountList){
                    account.toString();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }
    public List<Account> findAccount(int accountId){
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        List<Account> accountList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    accountList.add(new Account(
                            resultSet.getInt("account_id"),
                            resultSet.getString("account_name"),
                            resultSet.getInt("account_currency"),
                            resultSet.getString("account_type")
                    ));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }
    public int findAccountAddedId (){
        String sql= " select * from account order by account_id DESC LIMIT 1; " ;

        int idAccount = 0;
        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                idAccount= resultSet.getInt("account_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idAccount;
    }

    @Override
    public Account save(Account toSave) {
        String sql = "INSERT INTO account (account_name, account_currency, account_type) VALUES (?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, toSave.getAccountName());
            preparedStatement.setInt(2, toSave.getCurrency());
            preparedStatement.setString(3, toSave.getAccountType());

            int rowsAdded = preparedStatement.executeUpdate();

            Amount amount= new Amount(findAccountAddedId(), 0.0, LocalDateTime.now());
            amountDAO.save(amount);
            if (rowsAdded > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Crée un nouvel objet Account avec l'ID généré
                        Account savedAccount = new Account(
                                toSave.getAccountName(),
                                toSave.getCurrency(),
                                toSave.getAccountType()
                        );

                        return savedAccount;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Retourne null en cas d'échec de l'enregistrement
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        List<Account> accountList = new ArrayList<>();

        for (Account account : toSave) {
            Account saveAccount = save(account);
            if (saveAccount != null) {
                accountList.add(saveAccount);
            }

        }
        return accountList;
    }
    public Amount currentAmount(int accountId) {
        Amount lastAmount = null;
        String sql = "SELECT * FROM amount WHERE account_id = ? ORDER BY datetime DESC LIMIT 1;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastAmount = new Amount(
                        resultSet.getInt("account_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastAmount;
    }
    public double balanceAtADate(int accountId, LocalDateTime dateTime) {
        double amount = 0;

        String sql = "SELECT amount FROM amount WHERE account_id = ? AND datetime <= ? ORDER BY datetime DESC LIMIT 1;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, Timestamp.valueOf(dateTime));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    amount = resultSet.getDouble("amount");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }
}
