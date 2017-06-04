package com.kacperskory.cryptowallet.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrencyWallet {
    CryptoCurrency currency;
    BigDecimal totalAmount;
    BigDecimal currentUnitPriceInUsd;
    BigDecimal totalInvestedInUsd;
    BigDecimal currentTotalValueInUsd;
    BigDecimal profitInUsd;
}
