package com.Inholland.NovaBank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Controller
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(){
        //TODO: implement login
        return ResponseEntity.ok().body("login");
    }
}
