package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import com.Inholland.NovaBank.model.DTO.LoginResponseDTO;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService{


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    public User add(User user){

        return userRepository.save(user);
    }

    public User addUser(User user) {
        if (userRepository.findUserByUsername(user.getUsername()).isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("Username is already taken");
    }

    public User getById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAll(){
        return (List<User>) userRepository.findAll();
    }

    public User update(User user){
        return userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername()).orElseThrow(() -> new IllegalArgumentException("Username not found"));
        if(bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setToken(token);
            return loginResponseDTO;
        } else {
            throw new IllegalArgumentException("Password is incorrect");


        }

    }

    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElse(null);
    }
}
