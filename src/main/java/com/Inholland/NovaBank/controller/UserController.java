package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.newUserDTO;
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

    //role checken?
    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable long userId){
        try{
            User user = userService.getById(userId);
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

    public ResponseEntity<User> getUserByUsername(String username){
        try{
            User user = userService.getUserByUsername(username);
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

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
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
        try{
            return ResponseEntity.status(200).body(userService.getRemainingDailyLimit(userId));
        }
        catch (Exception e){
            return ResponseEntity.status(404).body(null);
        }
    }


    //andere controllers

}
