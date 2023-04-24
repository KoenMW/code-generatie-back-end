package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.service.BaseService;
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

    @GetMapping("/{IBAN}")
    public ResponseEntity<List<Transaction>> GetAllFromIban(@PathVariable String IBAN){
        if (BaseService.IsValidIban(IBAN)){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(transactionService.GetAllFromIban(IBAN));
    }

    @PostMapping
    public ResponseEntity<String> Add(@RequestBody Transaction transaction){
        if (transaction.getAmount() > 0 && BaseService.IsValidIban(transaction.getFromAccount()) && BaseService.IsValidIban(transaction.getToAccount())){
            transactionService.Add(transaction);
            return ResponseEntity.ok("Transaction added successfully (CODE: 200)");
        } else {
            return ResponseEntity.badRequest().body("Transaction not added (CODE: 400)");
        }
    }

    //haven't implemented withdraw and deposit yet
}
