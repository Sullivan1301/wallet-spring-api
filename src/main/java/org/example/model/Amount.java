package org.example.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Amount {
    private int accountId;
    private Double amount;
    private LocalDateTime dateTime;
}
