package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.IBANRequestBody;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transaction")
@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> GetAll(){
        return ResponseEntity.ok().body(transactionService.GetAll());
    }

    @GetMapping("/byIban")
    public ResponseEntity<List<Transaction>> GetAllFromIban(@RequestBody IBANRequestBody iban){
        if (!transactionService.IsValidIban(iban.getIban())){
            return ResponseEntity.accepted().build();
        }
        List<Transaction> transactions = transactionService.GetAllFromIban(iban.getIban());
        if (transactions.size() > 0)
        {
            return ResponseEntity.ok().body(transactions);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Transaction> Add(@RequestBody Transaction transaction){
        if (transactionService.ValidateTransaction(transaction) /*&& ValidateAuthorization(transaction.getToken())*/){
            transactionService.Add(transaction);
            return ResponseEntity.ok().body(transaction);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    //not implemented yet
    private boolean ValidateAuthorization(String token){
        return true;
    }
    //haven't implemented withdraw and deposit yet
}
