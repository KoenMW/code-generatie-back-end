package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import com.Inholland.NovaBank.model.DTO.LoginResponseDTO;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;

    @PostMapping
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try{
            return userService.login(loginRequestDTO);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }


    }
}
