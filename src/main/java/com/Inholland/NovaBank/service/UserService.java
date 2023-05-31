package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import com.Inholland.NovaBank.model.DTO.LoginResponseDTO;
import com.Inholland.NovaBank.model.DTO.newUserDTO;
import com.Inholland.NovaBank.model.DTO.returnUserDTO;
import com.Inholland.NovaBank.model.Role;
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


    public User getById(long id){
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }
    public List<User> getAll(){
        return (List<User>) userRepository.findAll();
    }

    public returnUserDTO addUser(newUserDTO user) {
        if (userRepository.findUserByUsername(user.getUsername()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User newUser = createUser(user);
            User savedUser = userRepository.save(newUser);
            return new returnUserDTO(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole(), savedUser.getDayLimit(), savedUser.getTransactionLimit(), savedUser.isHasAccount());
        }
        throw new IllegalArgumentException("Username is already taken");
    }

    private User createUser(newUserDTO user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setRole(Role.ROLE_USER);
        newUser.setDayLimit(5000);
        newUser.setTransactionLimit(2000);
        newUser.setHasAccount(false);
        return newUser;
    }

    public User update(User user){
        return userRepository.save(user);
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername());
        if(user == null) {
            throw new IllegalArgumentException("Username is incorrect");
        }
        if(bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setToken(token);
            return loginResponseDTO;
        } else {
            throw new IllegalArgumentException("Password is incorrect");
        }
    }

    UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }


}
