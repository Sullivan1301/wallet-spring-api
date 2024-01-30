package org.example.walletapi.controller;

import lombok.AllArgsConstructor;
import org.example.walletapi.DAO.AccountDAO;
import org.example.walletapi.model.Account;
import org.example.walletapi.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/accounts")
    public List<Account> getAccounts(){
        return accountService.getAccounts();
    }
}

