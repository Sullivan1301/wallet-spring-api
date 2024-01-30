package org.example.walletapi.model;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Account {
    private int accountId;
    private String accountName;
    private org.example.model.Amount amount;
    private List<org.example.model.Transaction> transactionList;
    private int currency;
    private String accountType;

    // constructor without accountId and amount initial= 0.0

    public Account(String accountName, int currency, String accountType) {
        this.accountName = accountName;
        this.currency = currency;
        this.accountType = accountType;
    }

    // constructor without amount and transaction list
    public Account(int accountId, String accountName, int currency, String accountType) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.currency = currency;
        this.accountType = accountType;
    }
}
