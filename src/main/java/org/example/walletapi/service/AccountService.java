package org.example.walletapi.service;

import org.example.walletapi.DAO.AccountDAO;
import org.example.walletapi.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    public List<Account> getAccounts(){
        return accountDAO.findAll();
    }
}
