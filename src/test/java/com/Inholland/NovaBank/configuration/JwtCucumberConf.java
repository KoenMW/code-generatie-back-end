package com.Inholland.NovaBank.configuration;

import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class JwtCucumberConf {

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    private UserService userService = new UserService();
    private final User user = generateUser();

    public String jwtToken = generateToken();

    private String generateToken(){
        return jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId());
    }

    private User generateUser(){
        return userService.addUser(new User("Jan", "Bank", "Jan", "1234", "novaBank@bank.nl", Role.ROLE_ADMIN, 3000, 1500, true));
    }

}
