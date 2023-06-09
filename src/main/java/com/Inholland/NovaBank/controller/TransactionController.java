package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.model.IBANRequestBody;
import com.Inholland.NovaBank.service.BaseService;
import com.Inholland.NovaBank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TransactionResponceDTO>> GetAll(){
        return ResponseEntity.ok().body(transactionService.GetAll());
    }


    @PostMapping
    public ResponseEntity<BaseDTO> Add(@RequestBody TransactionRequestDTO transaction){
        if (transactionService.ValidateTransaction(transaction)){
            return ResponseEntity.ok().body(transactionService.Add(transaction));
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("Error: Transaction not allowed. Please note that transfers to someone else's savings account are currently prohibited. Additionally, ensure that your transaction amount is within your daily transaction limit and account absolute limit. For more information or to adjust your limits, please contact our customer support.", 400));
        }
    }
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PostMapping("/withdraw")
    public ResponseEntity<BaseDTO> Withdraw(@RequestBody DepositWithdrawDTO dto){
        if (transactionService.ValidateWithdraw(dto)){
            transactionService.Withdraw(dto);
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("Error: Withdrawal not allowed. Please review your account balance to ensure sufficient funds are available for the transaction. Additionally, kindly verify that your withdrawal amount does not exceed your daily transaction limit or account absolute limit. For further assistance or to make adjustments to your limits, please contact our customer support.", 400));
        }
    }
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PostMapping("/deposit")
    public ResponseEntity<BaseDTO> Deposit(@RequestBody DepositWithdrawDTO dto){
        if (transactionService.ValidateDeposit(dto)){
            transactionService.Deposit(dto);
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("Error: Invalid deposit amount. Please ensure that the deposit amount is greater than zero and does not exceed 1000. For further assistance, please contact our customer support.", 400));
        }
    }

}
