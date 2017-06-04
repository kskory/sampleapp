package com.kacperskory.cryptowallet.service;

import com.kacperskory.cryptowallet.model.CryptoCurrency;

import java.math.BigDecimal;

public interface PriceCheckService {
    BigDecimal getCurrentPrice(CryptoCurrency currency);
}
