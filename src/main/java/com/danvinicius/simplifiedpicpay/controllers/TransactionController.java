package com.danvinicius.simplifiedpicpay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danvinicius.simplifiedpicpay.domain.transaction.Transaction;
import com.danvinicius.simplifiedpicpay.dto.TransactionDTO;
import com.danvinicius.simplifiedpicpay.exceptions.UserNotFoundException;
import com.danvinicius.simplifiedpicpay.services.TransactionService;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) throws UserNotFoundException {
        Transaction createdTransaction = this.transactionService.createTransaction(transaction);
        return ResponseEntity.ok().body(createdTransaction);

    }
    
}
