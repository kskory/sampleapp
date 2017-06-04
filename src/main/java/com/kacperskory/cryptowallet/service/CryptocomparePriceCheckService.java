package com.kacperskory.cryptowallet.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kacperskory.cryptowallet.model.CryptoCurrency;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CryptocomparePriceCheckService implements PriceCheckService {

    private static final String PRiCE_ENDPOINT = "https://min-api.cryptocompare.com/data/price";
    private RestTemplate restTemplate;

    public CryptocomparePriceCheckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BigDecimal getCurrentPrice(CryptoCurrency currency) {
        String url = PRiCE_ENDPOINT + "?fsym={currency}&tsyms=USD";
        CryptocompareResponse response = restTemplate.getForObject(url, CryptocompareResponse.class, currency);
        return response.getPriceInUsd();
    }

    @Data
    @NoArgsConstructor
    private static class CryptocompareResponse {
        @JsonProperty("USD")
        BigDecimal priceInUsd;
    }

}
