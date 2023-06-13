package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newUserDTO;
import com.Inholland.NovaBank.model.DTO.returnUserDTO;
import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private JwtTokenProvider jwttokenprovider;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,bCryptPasswordEncoder, jwttokenprovider);
    }

    @Test
    void getByIdDataSeeder() {

    }

    @Test
    void getById() {

    }

    @Test
    void authUser(){

    }

    @Test
    void transformUser(){

    }

    @Test
    void getByUsername() {

    }
    @Test
    void getAll() {
       /* when(userRepository.getAll(userService.getPageable(2L,0L))).thenReturn(
                List.of(
                        new returnUserDTO(1L, "John", "Doe", "JohnDoe", "John@doe.nl", Role.ROLE_ADMIN, 5000, 5000, true),
                        new returnUserDTO(2L, "Bank", "Bank", "Bank", "novaBank@bank.nl", Role.ROLE_USER, 3000, 5000, true)
                )
        );
        List<returnUserDTO> users = userService.getAll(2L,0L);
        assertNotNull(users);
        assertEquals(2, users.size());*/
    }

    @Test
    void addUser() {

    }

    @Test
    void createUser() {

    }

    @Test
    void update(){

    }

    @Test
    void login(){

    }

    @Test
    void GetSumOfAllTransactionsFromAccountOfLast24Hours(){

    }

    @Test
    void getRemainingDailyLimit(){

    }

}
