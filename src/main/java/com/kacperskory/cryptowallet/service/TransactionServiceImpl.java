package com.kacperskory.cryptowallet.service;

import com.kacperskory.cryptowallet.dao.TransactionDao;
import com.kacperskory.cryptowallet.model.CryptoCurrency;
import com.kacperskory.cryptowallet.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionDao transactionDao;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
        //add some sample data
        transactionDao.save(Transaction.builder().amount(BigDecimal.valueOf(12332,4)).currency(CryptoCurrency.ETH).date(Instant.now().toEpochMilli()).id(1L).type(Transaction.TransactionType.BUY).unitPriceInUsd(BigDecimal.valueOf(18532,2)).build());
        transactionDao.save(Transaction.builder().amount(BigDecimal.valueOf(15,4)).currency(CryptoCurrency.ETH).date(Instant.now().toEpochMilli()).id(2L).type(Transaction.TransactionType.BUY).unitPriceInUsd(BigDecimal.valueOf(12444,2)).build());
        transactionDao.save(Transaction.builder().amount(BigDecimal.valueOf(321,4)).currency(CryptoCurrency.ETH).date(Instant.now().toEpochMilli()).id(3L).type(Transaction.TransactionType.SELL).unitPriceInUsd(BigDecimal.valueOf(30001,2)).build());
        transactionDao.save(Transaction.builder().amount(BigDecimal.valueOf(34543,4)).currency(CryptoCurrency.BTC).date(Instant.now().toEpochMilli()).id(4L).type(Transaction.TransactionType.BUY).unitPriceInUsd(BigDecimal.valueOf(32100,2)).build());
    }

    @Override
    public List<Transaction> getAll() {
        return transactionDao.getAll();
    }

    @Override
    public Optional<Transaction> getById(long id) {
        return Optional.ofNullable(transactionDao.getById(id));
    }

    @Override
    public Transaction save(Transaction transaction) {
        Long id = transactionDao.save(transaction);
        transaction.setId(id);
        return transaction;
    }

    @Override
    public void deleteById(long id) {
        transactionDao.deleteById(id);
    }
}
