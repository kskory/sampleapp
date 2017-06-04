package com.kacperskory.cryptowallet.service;

import com.kacperskory.cryptowallet.model.CryptoCurrency;
import com.kacperskory.cryptowallet.model.CurrencyWallet;
import com.kacperskory.cryptowallet.model.Transaction;
import com.kacperskory.cryptowallet.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class WalletServiceImpl implements WalletService {


    private TransactionService transactionService;
    private PriceCheckService priceCheckService;

    @Autowired
    public WalletServiceImpl(TransactionService transactionService, PriceCheckService priceCheckService) {
        this.transactionService = transactionService;
        this.priceCheckService = priceCheckService;
    }

    @Override
    public Wallet getWallet() {
        Map<CryptoCurrency, List<Transaction>> byCurrency = transactionService.getAll().stream().collect(groupingBy(Transaction::getCurrency));

        List<CurrencyWallet> wallets = byCurrency.keySet().stream().map(currency -> {
            BigDecimal totalAmount = byCurrency.get(currency).stream().map(transaction ->
                    transaction.getType() == Transaction.TransactionType.BUY ? transaction.getAmount() : transaction.getAmount().negate()
            ).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalInvested = byCurrency.get(currency).stream()
                    .map(transaction -> {
                        BigDecimal transactionValue = transaction.getAmount().multiply(transaction.getUnitPriceInUsd());
                        if (transaction.getType() == Transaction.TransactionType.BUY) return transactionValue;
                        else return transactionValue.negate();
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal currentUnitPrice = priceCheckService.getCurrentPrice(currency);

            BigDecimal totalValue = currentUnitPrice.multiply(totalAmount);
            BigDecimal profit = totalValue.subtract(totalInvested);
            return CurrencyWallet.builder().currency(currency).totalAmount(totalAmount).currentUnitPriceInUsd(currentUnitPrice)
                    .totalInvestedInUsd(totalInvested).currentTotalValueInUsd(totalValue).profitInUsd(profit).build();
        }).collect(toList());


        BigDecimal totalPriceSpentInUsd = wallets.stream().map(CurrencyWallet::getTotalInvestedInUsd).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal currentTotalValueInUsd = wallets.stream().map(CurrencyWallet::getCurrentTotalValueInUsd).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal profitInUsd = currentTotalValueInUsd.subtract(totalPriceSpentInUsd);

        return Wallet.builder().currencyWallets(wallets)
                .totalInvestedtInUsd(totalPriceSpentInUsd)
                .currentTotalValueInUsd(currentTotalValueInUsd)
                .profitInUsd(profitInUsd)
                .build();

    }

}

