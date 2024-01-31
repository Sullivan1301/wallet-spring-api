package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;
import org.example.model.Amount;
import org.example.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionDAO implements DAOInterface<Transaction>{
    private DatabaseConnection connection;
    private AccountDAO accountDAO;
    private AmountDAO amountDAO;

    public TransactionDAO() {
        this.connection = new DatabaseConnection();
        this.accountDAO = new AccountDAO();
        this.amountDAO = new AmountDAO();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM transaction ;";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                transactionList.add(new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getInt("account_id"),
                        resultSet.getString("transaction_label"),
                        resultSet.getDouble("transaction_amount"),
                        resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                        resultSet.getString("transaction_type"),
                        resultSet.getInt("category_id")
                ));
                for(Transaction transaction : transactionList){
                    transaction.toString();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    public Transaction findTransactionAdded(){
        String sql= " select * from transaction order by transaction_id DESC LIMIT 1; " ;
        List<Transaction> transactionList = new ArrayList<>();
        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                transactionList.add(new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getInt("account_id"),
                        resultSet.getString("transaction_label"),
                        resultSet.getDouble("transaction_amount"),
                        resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                        resultSet.getString("transaction_type"),
                        resultSet.getInt("category_id")
                ));
                for(Transaction transaction : transactionList){
                    transaction.toString();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList.get(0);
    }
    @Override
    public Transaction save(Transaction toSave) {
        String sql = "INSERT INTO transaction (account_id, transaction_label, transaction_amount, transaction_date_hour, transaction_type, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, toSave.getAccountId());
            preparedStatement.setString(2, toSave.getTransactionLabel());
            preparedStatement.setDouble(3, toSave.getAmount());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(toSave.getDateTime()));
            preparedStatement.setString(5, toSave.getTransactionType());
            preparedStatement.setInt(6, toSave.getCategory());



            int rowsAdded = preparedStatement.executeUpdate();

                if (toSave.getTransactionType() == "debit"
                && accountDAO.currentAmount(toSave.getAccountId()).getAmount() > toSave.getAmount()){
                    System.out.println(accountDAO.currentAmount(toSave.getAccountId()));
                    Double debitAmount= accountDAO.currentAmount(toSave.getAccountId()).getAmount()
                            - toSave.getAmount();

                    Amount newAmount = new Amount(toSave.getAccountId(),debitAmount,
                            LocalDateTime.now());
                    amountDAO.save(newAmount);

                    return findTransactionAdded();
                }
                if (Objects.equals(toSave.getTransactionType(), "credit")){
                    System.out.println(accountDAO.currentAmount(toSave.getAccountId()));
                    Double creditAmount= accountDAO.currentAmount(toSave.getAccountId()).getAmount()
                            + toSave.getAmount();

                    Amount newAmount = new Amount(toSave.getAccountId(),creditAmount,
                            LocalDateTime.now());
                    amountDAO.save(newAmount);
                    return findTransactionAdded();
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> transactionList = new ArrayList<>();

        for (Transaction transaction : toSave) {
            Transaction saveTransaction = save(transaction);
            if (saveTransaction != null) {
                transactionList.add(saveTransaction);
            }

        }
        return transactionList;
    }
}
