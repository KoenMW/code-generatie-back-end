package com.Inholland.NovaBank.configuration;

import com.Inholland.NovaBank.model.DTO.newUserDTO;
import com.Inholland.NovaBank.model.DTO.returnUserDTO;
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
    private returnUserDTO user2 = generateUser();
    private User user = userService.getUserByUsername("jan");

    public String jwtToken = generateToken();

    private String generateToken(){
        return jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId());
    }

    private returnUserDTO generateUser(){
        return userService.addUser(new newUserDTO("jan", "jansen", "jan", "1234", "henk@gmail.com"));
    }

}
