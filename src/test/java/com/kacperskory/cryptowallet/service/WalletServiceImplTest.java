package com.kacperskory.cryptowallet.service;

import com.kacperskory.cryptowallet.model.CryptoCurrency;
import com.kacperskory.cryptowallet.model.Transaction;
import com.kacperskory.cryptowallet.model.Wallet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceImplTest {
    private static final BigDecimal ETH_AMOUNT_BUY_1 = BigDecimal.valueOf(12332, 4);
    private static final BigDecimal ETH_AMOUNT_BUY_2 = BigDecimal.valueOf(15, 4);
    private static final BigDecimal ETH_AMOUNT_SELL_1 = BigDecimal.valueOf(321, 4);
    private static final BigDecimal BTC_AMOUNT_BUY = BigDecimal.valueOf(34543, 4);
    private static final BigDecimal ETH_PRICE_BUY_1 = BigDecimal.valueOf(18532, 2);
    private static final BigDecimal ETH_PRICE_BUY_2 = BigDecimal.valueOf(12444, 2);
    private static final BigDecimal ETH_PRICE_SELL_1 = BigDecimal.valueOf(30001, 2);
    private static final BigDecimal BTC_PRICE_BUY = BigDecimal.valueOf(32100, 2);
    private static final BigDecimal CURRENT_BTC_PRICE = BigDecimal.valueOf(250043, 2);
    private static final BigDecimal CURRENT_ETH_PRICE = BigDecimal.valueOf(24785, 2);
    @Mock
    private TransactionService transactionServiceMock;
    @Mock
    private PriceCheckService priceCheckServiceMock;

    private WalletService service;

    @Before
    public void setup() {
        initMocks(WalletServiceImpl.class);
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder().amount(ETH_AMOUNT_BUY_1).currency(CryptoCurrency.ETH).date(Instant.now().toEpochMilli()).id(1L).type(Transaction.TransactionType.BUY).unitPriceInUsd(ETH_PRICE_BUY_1).build(),
                Transaction.builder().amount(ETH_AMOUNT_BUY_2).currency(CryptoCurrency.ETH).date(Instant.now().toEpochMilli()).id(2L).type(Transaction.TransactionType.BUY).unitPriceInUsd(ETH_PRICE_BUY_2).build(),
                Transaction.builder().amount(ETH_AMOUNT_SELL_1).currency(CryptoCurrency.ETH).date(Instant.now().toEpochMilli()).id(3L).type(Transaction.TransactionType.SELL).unitPriceInUsd(ETH_PRICE_SELL_1).build(),
                Transaction.builder().amount(BTC_AMOUNT_BUY).currency(CryptoCurrency.BTC).date(Instant.now().toEpochMilli()).id(4L).type(Transaction.TransactionType.BUY).unitPriceInUsd(BTC_PRICE_BUY).build());
        service = new WalletServiceImpl(transactionServiceMock, priceCheckServiceMock);

        when(priceCheckServiceMock.getCurrentPrice(CryptoCurrency.BTC)).thenReturn(CURRENT_BTC_PRICE);
        when(priceCheckServiceMock.getCurrentPrice(CryptoCurrency.ETH)).thenReturn(CURRENT_ETH_PRICE);
        when(transactionServiceMock.getAll()).thenReturn(transactions);
    }

    @Test
    public void shouldComputeCurrencyWallets() {
        Wallet wallet = service.getWallet();

        assertEquals(2, wallet.getCurrencyWallets().size());

        wallet.getCurrencyWallets().forEach(cur -> {
            if (cur.getCurrency() == CryptoCurrency.ETH) {
                BigDecimal totalAmount = ETH_AMOUNT_BUY_1.add(ETH_AMOUNT_BUY_2).subtract(ETH_AMOUNT_SELL_1);
                assertEquals(totalAmount, cur.getTotalAmount());
                BigDecimal totalInvested = ETH_AMOUNT_BUY_1.multiply(ETH_PRICE_BUY_1).add(ETH_AMOUNT_BUY_2.multiply(ETH_PRICE_BUY_2)).subtract(ETH_AMOUNT_SELL_1.multiply(ETH_PRICE_SELL_1));
                assertEquals(totalInvested, cur.getTotalInvestedInUsd());
                BigDecimal currentTotalValue = totalAmount.multiply(CURRENT_ETH_PRICE);
                assertEquals(currentTotalValue, cur.getCurrentTotalValueInUsd());
                assertEquals(currentTotalValue.subtract(totalInvested), cur.getProfitInUsd());
            }
        });
    }

    @Test
    public void shouldComputeWalletTotalValues() {
        Wallet wallet = service.getWallet();

        BigDecimal totalInvested = ETH_AMOUNT_BUY_1.multiply(ETH_PRICE_BUY_1).add(ETH_AMOUNT_BUY_2.multiply(ETH_PRICE_BUY_2)).subtract(ETH_AMOUNT_SELL_1.multiply(ETH_PRICE_SELL_1)).add(BTC_AMOUNT_BUY.multiply(BTC_PRICE_BUY));
        BigDecimal currentTotalValueEth = ETH_AMOUNT_BUY_1.add(ETH_AMOUNT_BUY_2).subtract(ETH_AMOUNT_SELL_1).multiply(CURRENT_ETH_PRICE);
        BigDecimal currentTotalValueBtc = BTC_AMOUNT_BUY.multiply(CURRENT_BTC_PRICE);

        assertEquals(currentTotalValueBtc.add(currentTotalValueEth).subtract(totalInvested), wallet.getProfitInUsd());
        assertEquals(totalInvested, wallet.getTotalInvestedInUsd());
    }

}