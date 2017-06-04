package com.kacperskory.cryptowallet.controller;

import com.kacperskory.cryptowallet.model.Transaction;
import com.kacperskory.cryptowallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }


    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> getTransaction(@PathVariable("id") long id) {
        Optional<Transaction> transaction = transactionService.getById(id);
        return transaction
                .map(transaction1 -> new ResponseEntity<>(transaction1, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transactionToSave, UriComponentsBuilder ucBuilder) {
        Transaction savedTransaction = transactionService.save(transactionToSave);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/transactions/{id}").buildAndExpand(savedTransaction.getId()).toUri());
        return new ResponseEntity<>(savedTransaction, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") long id) {
        transactionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
