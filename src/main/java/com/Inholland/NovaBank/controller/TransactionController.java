package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.IBANRequestBody;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.service.BaseService;
import com.Inholland.NovaBank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.Inholland.NovaBank.model.UserIdRequestBody;

import java.util.List;

@RequestMapping("/transactions")
@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Transaction>> GetAll(){
        return ResponseEntity.ok().body(transactionService.GetAll());
    }

    @GetMapping("/byIban")
    public ResponseEntity<List<Transaction>> GetAllFromIban(@RequestBody IBANRequestBody iban){
        if (!BaseService.IsValidIban(iban.getIban())){
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
        if (transactionService.ValidateTransaction(transaction)){
            Transaction completedTransaction = transactionService.Add(transaction);
            return ResponseEntity.ok().body(completedTransaction);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<Transaction>> GetAllFromUser(@RequestBody UserIdRequestBody id){
        List<Transaction> transactions = transactionService.GetAllFromUser(id.getUserId());
        if (transactions.size() > 0)
        {
            return ResponseEntity.ok().body(transactions);
        }
        return ResponseEntity.noContent().build();
    }
    //haven't implemented withdraw and deposit yet
}
