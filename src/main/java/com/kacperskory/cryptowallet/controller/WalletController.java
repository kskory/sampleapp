package com.kacperskory.cryptowallet.controller;

import com.kacperskory.cryptowallet.model.Transaction;
import com.kacperskory.cryptowallet.model.Wallet;
import com.kacperskory.cryptowallet.service.TransactionService;
import com.kacperskory.cryptowallet.service.WalletService;
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
public class WalletController {

    WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    public ResponseEntity<Wallet> getWallet() {
        return new ResponseEntity<>(walletService.getWallet(), HttpStatus.OK);
    }

}
