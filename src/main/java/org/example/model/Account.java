package org.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Account {
    private int accountId;
    private String accountName;
    private int currency;
    private String accountType;

    public Account( String accountName, int currency, String accountType) {
        this.accountName = accountName;
        this.currency = currency;
        this.accountType = accountType;
    }
}
