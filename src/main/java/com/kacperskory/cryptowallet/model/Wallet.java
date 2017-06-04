package com.kacperskory.cryptowallet.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Wallet {
    List<CurrencyWallet> currencyWallets;

    BigDecimal totalInvestedtInUsd;
    BigDecimal currentTotalValueInUsd;
    BigDecimal profitInUsd;
}
