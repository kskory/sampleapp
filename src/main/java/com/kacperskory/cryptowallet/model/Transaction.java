package com.kacperskory.cryptowallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( unique = true, nullable = false)
    Long id;

    @Column(name = "currency")
    CryptoCurrency currency;

    @Column(name = "amount", precision = 20, scale = 8)
    BigDecimal amount;

    @Column(name = "unitPriceInUsdCents", precision = 20, scale = 8)
    BigDecimal unitPriceInUsd;

    @Column(name = "type")
    TransactionType type;

    @Column(name = "date")
    long date;

    public enum TransactionType {
        BUY, SELL
    }
}
