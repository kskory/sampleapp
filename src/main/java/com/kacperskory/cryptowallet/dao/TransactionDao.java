package com.kacperskory.cryptowallet.dao;

import com.kacperskory.cryptowallet.model.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getAll();

    Transaction getById(long id);

    Long save(Transaction transaction);

    void deleteById(long id);
}
