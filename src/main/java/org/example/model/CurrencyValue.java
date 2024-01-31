package org.example.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CurrencyValue {
    private int currencyId;
    private int currencyIdSource;
    private int currencyIdDestination;
    private double value;
    private LocalDateTime effectDate;

    public CurrencyValue(int currencyIdSource, int currencyIdDestination, double value, LocalDateTime effectDate) {
        this.currencyIdSource = currencyIdSource;
        this.currencyIdDestination = currencyIdDestination;
        this.value = value;
        this.effectDate = effectDate;
    }
}
