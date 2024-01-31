package org.example;

import org.example.DAO.*;
import org.example.model.Account;
import org.example.model.Category;
import org.example.model.CurrencyValue;
import org.example.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args){
        TransactionDAO transactionDAO = new TransactionDAO();
        System.out.println("find all transaction");
        System.out.println(transactionDAO.findAll());
        AmountDAO amountDAO = new AmountDAO();
        System.out.println("find all amount");
        System.out.println(amountDAO.findAll());
        Transaction debit = new Transaction(2, "cadeau de noel", 50000.0, LocalDateTime.now()
                , "debit", 2);
        transactionDAO.save(debit);
        System.out.println(transactionDAO.findAll());

    }
}