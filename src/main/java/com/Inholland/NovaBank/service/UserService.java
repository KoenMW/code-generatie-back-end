package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends BaseService{

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    public returnUserDTO getByIdDataSeeder(long id){
        return transformUser(userRepository.findById(id).orElse(null));
    }
    public returnUserDTO getById(long id){
        if (authUser(id)) {
            return transformUser(userRepository.findById(id).orElse(null));
        }
        else {
            throw new IllegalArgumentException("Not authorized");
        }
    }
    private boolean authUser(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentPrincipalName = authentication.getName();
        returnUserDTO user = getUserByUsername(currentPrincipalName);
        if(user.getRole().toString().equals("ROLE_ADMIN")){
            return true;
        } else return user.getId() == id;
    }

    private returnUserDTO transformUser(User user) {
        return new returnUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getDayLimit(), user.getTransactionLimit(), user.isHasAccount());
    }
    public returnUserDTO getUserByUsername(String username){
        return transformUser(userRepository.findUserByUsername(username));
    }
    public List<returnUserDTO> getAll(boolean isActive, Long limit, Long offset){
        if (limit == null) {
            limit = 1000L;
        }
        if (offset == null) {
            offset = 0L;
        }

        if (isActive) {
            return AllUsersWithoutAccount(limit, offset, true);
        } else {
            return getAll(limit, offset);
        }
    }
    public List<returnUserDTO> getAll(Long limit, Long offset){
        List <User> users = (List <User>) userRepository.getAll(getPageable(limit, offset));
        return transformUsers(users);
    }
    public List<returnUserDTO> AllUsersWithoutAccount(Long limit, Long offset, boolean active){
        return transformUsers(userRepository.findAllUsersWithoutAccount(getPageable(limit, offset), active));
    }
    private Pageable getPageable(Long limit, Long offset) {
        return new OffsetBasedPageRequest(offset.intValue(), limit.intValue());
    }
    public List<returnUserDTO> transformUsers (List<User> users){
        List <returnUserDTO> userDTOList = new ArrayList<>();
        for (User user: users) {
             userDTOList.add(new returnUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getDayLimit(), user.getTransactionLimit(), user.isHasAccount()));
        }
        return userDTOList;
    }

    public returnUserDTO addUser(newUserDTO user) {
        if (userRepository.findUserByUsername(user.getUsername()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User newUser = createUser(user);
            User savedUser = userRepository.save(newUser);
            return new returnUserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole(), savedUser.getDayLimit(), savedUser.getTransactionLimit(), savedUser.isHasAccount());
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

    public returnUserDTO update(patchUserDTO user){
        User userFromRepo = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Username not found"));
            if(userFromRepo != null) {
                switch (user.getKey()) {
                    case "firstName" -> userFromRepo.setFirstName(user.getValue());
                    case "lastName" -> userFromRepo.setLastName(user.getValue());
                    case "username" -> userFromRepo.setUsername(user.getValue());
                    case "email" -> userFromRepo.setEmail(user.getValue());
                    case "role" -> userFromRepo.setRole((Role) Role.valueOf(user.getValue()));
                    case "dayLimit" -> userFromRepo.setDayLimit(Integer.parseInt(user.getValue()));
                    case "transactionLimit" -> userFromRepo.setTransactionLimit(Integer.parseInt(user.getValue()));
                    case "hasAccount" -> userFromRepo.setHasAccount(Boolean.parseBoolean(user.getValue()));
                    default -> {
                        return null;
                    }
                }
            }
            User savedUser = userRepository.save(userFromRepo);
            return new returnUserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole(), savedUser.getDayLimit(), savedUser.getTransactionLimit(), savedUser.isHasAccount());
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername());

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
