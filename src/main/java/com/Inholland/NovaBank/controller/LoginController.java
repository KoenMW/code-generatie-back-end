package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import com.Inholland.NovaBank.model.DTO.LoginResponseDTO;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;


    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try{
            return ResponseEntity.ok().body(userService.login(loginRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }


    }
}
