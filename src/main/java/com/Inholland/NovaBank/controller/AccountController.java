package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.Account;
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
    public List<Account> getAll(){
        return accountService.getAll();
    }



    @GetMapping("/{id}")
    public Account getById(@PathVariable long id){
        return accountService.getById(id);
    }
    @GetMapping("/user/{id}")
    public List<Account> getByUserId(@PathVariable long id){
        return accountService.getByUserId(id);
    }
    @PostMapping
    public Account add(@RequestBody Account account){
        return accountService.add(account);
    }

    @PatchMapping ()
    public Account update(@RequestBody Account account){

        return accountService.update(account);
    }

    @PatchMapping("/delete/{id}")
    public Account setInactive(@RequestBody Account account, @PathVariable long id){
        Account accountFromService = accountService.getById(id);
        accountFromService.setStatus(false);

        return accountService.update(accountFromService);
    }

}
