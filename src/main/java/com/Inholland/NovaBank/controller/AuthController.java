package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import com.Inholland.NovaBank.model.DTO.LoginResponseDTO;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@Controller
public class AuthController {
    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try{
            return ResponseEntity.ok().body(userService.login(loginRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }


    }
}
