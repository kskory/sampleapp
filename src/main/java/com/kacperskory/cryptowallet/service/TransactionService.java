package com.kacperskory.cryptowallet.service;

import com.kacperskory.cryptowallet.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> getAll();

    Optional<Transaction> getById(long id);

    Transaction save(Transaction transaction);

    void deleteById(long id);
}
