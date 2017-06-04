package com.kacperskory.cryptowallet.model;

public enum CryptoCurrency {
    BTC("Bitcoin"), ETH("Ether"), SC("Siacoin");

    private final String name;

    CryptoCurrency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
