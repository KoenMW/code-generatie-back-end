package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAll(@RequestParam (required = false) boolean isActive,@RequestParam (required = false) Long limit, @RequestParam (required = false) Long offset){
        return accountService.getAll(isActive, limit, offset);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> getByUserId(@PathVariable long userId){
        return ResponseEntity.status(200).body(accountService.getByUserId(userId));
    }



    @PostMapping
    public ResponseEntity<returnAccountDTO>add(@RequestBody newAccountDTO account){
        return ResponseEntity.status(201).body(accountService.add(account));
    }

    @PatchMapping ()
    public Account update(@RequestBody Account account){

        return accountService.update(account);
    }



}
