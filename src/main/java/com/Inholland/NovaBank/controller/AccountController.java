package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.*;

import com.Inholland.NovaBank.service.AccountService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Account>> getAll(@RequestParam (required = false) boolean isActive,@RequestParam (required = false) Long limit, @RequestParam (required = false) Long offset){
        try{
            return ResponseEntity.status(200).body(accountService.getAll(isActive, limit, offset));
        }catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PreAuthorize("hasRole('USER')" + " || hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<searchAccountDTO>> getAllSearch(@RequestParam (required = false) Long limit, @RequestParam (required = false) Long offset){
        try{
            return ResponseEntity.status(200).body(accountService.getAllSearch(limit, offset));
        }catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }


    @PreAuthorize("hasRole('USER')" + " || hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> getByUserId(@PathVariable long userId){
        try{
            List<Account> accounts = accountService.getByUserId(userId);

            if(accounts.isEmpty()){
                return ResponseEntity.status(404).body(null);
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
    public ResponseEntity<BaseDTO>add(@RequestBody newAccountDTO account){
        try{
            return ResponseEntity.status(201).body(accountService.add(account));
        }catch (Exception e){
            return ResponseEntity.status(400).body(new ErrorDTO(e.getMessage(), 400));
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping ()
    public ResponseEntity<BaseDTO> update(@RequestBody patchAccountDTO account){
    try{
        if(account.getOp().equalsIgnoreCase("update")){
            return ResponseEntity.status(200).body(accountService.update(account));
        }
        else{
            return ResponseEntity.status(404).body(new ErrorDTO("Operation not found", 404));
        }
    }
    catch (Exception e){
        return ResponseEntity.status(404).body(new ErrorDTO(e.getMessage(), 404));
    }


    }

}
