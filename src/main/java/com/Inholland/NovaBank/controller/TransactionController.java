package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.DTO.BaseDTO;
import com.Inholland.NovaBank.model.DTO.DepositWithdrawDTO;
import com.Inholland.NovaBank.model.DTO.TransactionRequestDTO;
import com.Inholland.NovaBank.model.DTO.TransactionResponceDTO;
import com.Inholland.NovaBank.model.IBANRequestBody;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.service.BaseService;
import com.Inholland.NovaBank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
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
    public ResponseEntity<List<TransactionResponceDTO>> GetAllFromIban(@RequestBody IBANRequestBody iban){
        if (!BaseService.IsValidIban(iban.getIban())){
            return ResponseEntity.accepted().build();
        }
        List<TransactionResponceDTO> transactions = transactionService.GetAllFromIban(iban.getIban());
        if (transactions.size() > 0)
        {
            return ResponseEntity.ok().body(transactions);
        }
        return ResponseEntity.noContent().build();
    }



    @PostMapping
    public ResponseEntity<BaseDTO> Add(@RequestBody TransactionRequestDTO transaction){
        if (transactionService.ValidateTransaction(transaction)){
            return ResponseEntity.ok().body(transactionService.Add(transaction));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('USER')" + " || hasRole('ADMIN')")
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<TransactionResponceDTO>> GetAllFromUser(@PathVariable String userId){
        long id = Long.parseLong(userId);
        List<TransactionResponceDTO> transactions = transactionService.GetAllFromUser(id);
        if (transactions.size() > 0)
        {
            return ResponseEntity.ok().body(transactions);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BaseDTO> Withdraw(@RequestBody DepositWithdrawDTO dto){
        if (transactionService.ValidateWithdraw(dto)){
            transactionService.withdraw(dto);
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<BaseDTO> Deposit(@RequestBody DepositWithdrawDTO dto){
        if (transactionService.ValidateDeposit(dto)){
            transactionService.deposit(dto);
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
