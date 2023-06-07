package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.BaseDTO;
import com.Inholland.NovaBank.model.DTO.newUserDTO;
import com.Inholland.NovaBank.model.DTO.patchUserDTO;
import com.Inholland.NovaBank.model.DTO.returnUserDTO;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.service.TransactionService;
import com.Inholland.NovaBank.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN')" + " || hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<BaseDTO> getById(@PathVariable long userId){
        try{

            returnUserDTO user = userService.getById(userId);

            if(user == null){
                return ResponseEntity.status(403).body(null);
            }
            else{
                return ResponseEntity.status(200).body(user);
            }
        }catch (Exception e) {

            return ResponseEntity.status(404).body(null);

        }
    }

    @PreAuthorize("hasRole('ADMIN')" + " || hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<returnUserDTO>> getAll(@RequestParam (required = false) boolean isActive,@RequestParam (required = false) Long limit, @RequestParam (required = false) Long offset){
        try {

            return ResponseEntity.status(200).body(userService.getAll(isActive, limit, offset));
        }catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<BaseDTO> update(@RequestBody patchUserDTO user){
        try{
            return ResponseEntity.status(200).body(userService.update(user));
        }catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<returnUserDTO>add(@RequestBody newUserDTO user){
        try{
            return ResponseEntity.status(200).body(userService.addUser(user));
        }catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/dailylimit/{userId}")
    public ResponseEntity<Double> getRemainingDailyLimit(@PathVariable long userId){
        System.out.println("test");
        System.out.println(userId);
        try{
            return ResponseEntity.status(200).body(userService.getRemainingDailyLimit(userId));

        }
        catch (Exception e){
            System.out.println("failed to retrieve daily limit");
            System.out.println(e);
            return ResponseEntity.status(404).body(null);
        }
    }
}
