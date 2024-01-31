package org.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Currency {
    private int currencyId;
    private String currencyName;
    private String currencyCode;

    public Currency( String currencyName, String currencyCode) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
    }
}
