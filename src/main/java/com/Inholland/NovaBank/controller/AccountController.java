package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ObjectMapper objectMapper;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Account>> getAll(@RequestParam (required = false) boolean isActive,@RequestParam (required = false) Long limit, @RequestParam (required = false) Long offset){
        try{
            return accountService.getAll(isActive, limit, offset);
        }catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PreAuthorize("hasRole('USER')" + " || hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> getByUserId(@PathVariable long userId){
        try{
            List<Account> accounts = accountService.getByUserId(userId);
            if(accounts == null){
                return ResponseEntity.status(403).body(null);
            }
            else{
                return ResponseEntity.status(200).body(accounts);
            }

        }
        catch (Exception e){
            return ResponseEntity.status(404).body(null);
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<returnAccountDTO>add(@RequestBody newAccountDTO account){
        try{
            return ResponseEntity.status(201).body(accountService.add(account));
        }catch (Exception e){
            return ResponseEntity.status(400).body(null);
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping ()
    public ResponseEntity<returnAccountDTO> update(@RequestBody patchAccountDTO account){
    try{
        if(account.getOp().equalsIgnoreCase("update")){
            return ResponseEntity.status(200).body(accountService.update(account));
        }
        else{
            return ResponseEntity.status(404).body(null);
        }
    }
    catch (Exception e){
        return ResponseEntity.status(404).body(null);
    }


    }





}
