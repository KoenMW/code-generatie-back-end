package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    public List<User> getAll(){
        try{
            return userService.getAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }
}
