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
    public ResponseEntity<String> Add(@RequestBody Transaction transaction){
        if (transaction.getAmount() > 0 && transactionService.IsValidIban(transaction.getFromAccount()) && transactionService.IsValidIban(transaction.getToAccount()) && transactionService.HasBalance(transaction.getFromAccount(), transaction.getAmount())){
            transactionService.Add(transaction);
            return ResponseEntity.ok("Transaction added successfully (CODE: 200)");
        } else {
            return ResponseEntity.badRequest().body("Transaction not added (CODE: 400)");
        }
    }
    //haven't implemented withdraw and deposit yet
}
