package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.Inholland.NovaBank.model.Role.ROLE_ADMIN;
import static com.Inholland.NovaBank.model.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @MockBean
    private UserService userServiceMock;
    @InjectMocks
    private UserService userService;

    @Test
    void getByIdDataSeeder() {
        when(userServiceMock.getByIdDataSeeder(1L)).thenReturn(new returnUserDTO(1L, "Jan", "Bakker", "JanBakker", "Jan@bakker.nl", ROLE_USER, 5000, 1000, true));
        returnUserDTO user = userServiceMock.getByIdDataSeeder(1L);
        assertNotNull(user);
    }

    @Test
    void getByIdDataSeederInvalid(){
        when(userServiceMock.getByIdDataSeeder(1L)).thenReturn(null);
        returnUserDTO user = userServiceMock.getByIdDataSeeder(1L);
        assertNull(user);
    }

    //Login nodig dus userServiceMock
    @Test
    void getById() {
        when(userServiceMock.getById(1L)).thenReturn(new returnUserDTO(1L, "Jan", "Bakker", "JanBakker", "Jan@Bakker.nl", ROLE_USER, 5000, 1000, true));
        returnUserDTO user = userServiceMock.getById(1L);
        assertNotNull(user);
    }
    @Test
    void getByIdInvalid(){
        when(userServiceMock.getById(1L)).thenReturn(null);
        returnUserDTO user = userServiceMock.getById(1L);
        assertNull(user);
    }

    @Test
    void authUser(){
        when(userServiceMock.authUser(1L)).thenReturn(true);
    }
    @Test
    void authUserInvalid(){
        when(userServiceMock.authUser(1L)).thenReturn(false);
    }

    @Test
    void transformUser(){
        when(userServiceMock.transformUser(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true))).thenReturn(new returnUserDTO(1L, "firstName", "lastName", "username", "email", ROLE_USER, 5000, 1000, true));
        returnUserDTO user = userServiceMock.transformUser(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true));
        assertNotNull(user);
    }
    @Test
    void transformUserInvalid(){
        when(userServiceMock.transformUser(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true))).thenReturn(null);
        returnUserDTO user = userServiceMock.transformUser(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true));
        assertNull(user);
    }

    @Test
    void getByUsername() {
        when(userServiceMock.getUserByUsername("JanBakker")).thenReturn(new returnUserDTO(1L, "Jan", "Bakker", "JanBakker", "Jan@Bakker.nl", ROLE_USER, 5000, 1000, true));
        assertNotNull(userServiceMock.getUserByUsername("JanBakker"));
    }
    @Test
    void getByUsernameInvalid(){
        when(userServiceMock.getUserByUsername("JanBakker")).thenReturn(null);
        assertNull(userServiceMock.getUserByUsername("JanBakker"));
    }

    @Test
    void getAll() {
        when(userServiceMock.getAll(true, 1000L, 0L)).thenReturn(List.of(new returnUserDTO(1L, "Jan", "Bakker", "JanBakker", "Jan@Bakker.nl", ROLE_USER, 5000, 1000, true)));
        List<returnUserDTO> users = userServiceMock.getAll(true, 1000L, 0L);
        assertNotNull(users);
    }
    @Test
    void getAllInvalid(){
        when(userServiceMock.getAll(true, 1000L, 0L)).thenReturn(null);
        List<returnUserDTO> users = userServiceMock.getAll(true, 1000L, 0L);
        assertNull(users);
    }

    @Test
    void getAllUsers(){
        when(userServiceMock.getAll(1000L, 0L)).thenReturn(List.of(new returnUserDTO(1L, "Jan", "Bakker", "JanBakker", "Jan@Bakker.nl", ROLE_USER, 5000, 1000, true)));
        List<returnUserDTO> users = userServiceMock.getAll(1000L, 0L);
        assertNotNull(users);
    }
    @Test
    void getAllUsersInvalid(){
        when(userServiceMock.getAll(1000L, 0L)).thenReturn(null);
        List<returnUserDTO> users = userServiceMock.getAll(1000L, 0L);
        assertNull(users);
    }

    @Test
    void AllUsersWithoutAccount(){
        when(userServiceMock.AllUsersWithoutAccount(1000L, 0L, true)).thenReturn(List.of(new returnUserDTO(1L, "Jan", "Bakker", "JanBakker", "Jan@Bakker.nl", ROLE_USER, 5000, 1000, true)));
        List<returnUserDTO> users = userServiceMock.AllUsersWithoutAccount(1000L, 0L, true);
        assertNotNull(users);
    }
    @Test
    void AllUsersWithoutAccountInvalid(){
        when(userServiceMock.AllUsersWithoutAccount(1000L, 0L, true)).thenReturn(null);
        List<returnUserDTO> users = userServiceMock.AllUsersWithoutAccount(1000L, 0L, true);
        assertNull(users);
    }

    @Test
    void getPageable() {
        Pageable pageable = userService.getPageable(1000L,0L);
        assertNotNull(pageable);
    }
    @Test
    void getPageableInvalid(){
        when(userServiceMock.getPageable(1000L,0L)).thenReturn(null);
        Pageable pageable = userServiceMock.getPageable(1000L,0L);
        assertNull(pageable);
    }

    @Test
    void transformUsers(){
        when(userServiceMock.transformUsers(List.of(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true)))).thenReturn(List.of(new returnUserDTO(1L, "firstName", "lastName", "username", "email", ROLE_USER, 5000, 1000, true)));
        List<returnUserDTO> users = userServiceMock.transformUsers(List.of(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true)));
        assertNotNull(users);
    }
    @Test
    void transformUsersInvalid(){
        when(userServiceMock.transformUsers(List.of(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true)))).thenReturn(null);
        List<returnUserDTO> users = userServiceMock.transformUsers(List.of(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true)));
        assertNull(users);
    }

    @Test
    void addUser() {
        when(userServiceMock.addUser(new newUserDTO("firstName", "lastName", "username", "password", "email"))).thenReturn(new returnUserDTO(1L, "firstName", "lastName", "username", "email", ROLE_USER, 5000, 1000, true));
        returnUserDTO user = userServiceMock.addUser(new newUserDTO("firstName", "lastName", "username", "password", "email"));
        assertNotNull(user);
    }
    @Test
    void addUserInvalid(){
        when(userServiceMock.addUser(new newUserDTO("firstName", "lastName", "username", "password", "email"))).thenReturn(null);
        returnUserDTO user = userServiceMock.addUser(new newUserDTO("firstName", "lastName", "username", "password", "email"));
        assertNull(user);
    }

    @Test
    void validEmail(){
        boolean valid = userService.validEmail("test@testmail.com");
        assertTrue(valid);
    }
    @Test
    void validEmailInvalid(){
        boolean valid = userService.validEmail("test@testmail");
        assertFalse(valid);
    }

    @Test
    void checkIfNotNull(){
        boolean valid = userService.checkIfNotNull(new newUserDTO("firstName", "lastName", "username", "password", "email"));
        assertTrue(valid);
    }
    @Test
    void checkIfNotNullInvalid(){
        boolean valid = userService.checkIfNotNull(new newUserDTO("", "lastName", "username", "password", "email"));
        assertFalse(valid);
    }

    @Test
    void checkUsername(){
        when(userServiceMock.checkUsername("Jan-Willem")).thenReturn(true);
        assertTrue(userServiceMock.checkUsername("Jan-Willem"));
    }
    @Test
    void checkUsernameInvalid(){
        boolean valid = userServiceMock.checkUsername("JohnDoe");
        assertFalse(valid);
    }

    @Test
    void checkUsernameLength(){
        boolean valid = userService.checkUsernameLength("username");
        assertTrue(valid);
    }
    @Test
    void checkUsernameLengthInvalid(){
        boolean valid = userService.checkUsernameLength("use");
        assertFalse(valid);
    }

    @Test
    void createUser() {
        when(userServiceMock.createUser(new newUserDTO("firstName", "lastName", "username", "password", "email"))).thenReturn(new User("firstName", "lastName", "username", "password", "email", ROLE_USER, 5000, 1000, true));
        User user = userServiceMock.createUser(new newUserDTO("firstName", "lastName", "username", "password", "email"));
        assertNotNull(user);
    }
    @Test
    void createUserInvalid(){
        when(userServiceMock.createUser(new newUserDTO("firstName", "lastName", "username", "password", "email"))).thenReturn(null);
        User user = userServiceMock.createUser(new newUserDTO("firstName", "lastName", "username", "password", "email"));
        assertNull(user);
    }

    @Test
    void update(){
        when(userServiceMock.update(new patchUserDTO(1L, "update", "dayLimit", "320"))).thenReturn(new returnUserDTO(1L, "firstName", "lastName", "username", "email", ROLE_USER, 5000, 1000, true));
        returnUserDTO user = userServiceMock.update(new patchUserDTO(1L, "update", "dayLimit", "320"));
        assertNotNull(user);
    }
    @Test
    void updateInvalid(){
        when(userServiceMock.update(new patchUserDTO(1L, "update", "dayLimit", "320"))).thenReturn(null);
        returnUserDTO user = userServiceMock.update(new patchUserDTO(1L, "update", "dayLimit", "320"));
        assertNull(user);
    }

    @Test
    void checkLimit(){
        boolean valid = userService.checkLimit(1234);
        assertTrue(valid);
    }
    @Test
    void checkLimitInvalid(){
        boolean valid = userService.checkLimit(123456789);
        assertFalse(valid);
    }

    @Test
    void login(){
        when(userServiceMock.login(new LoginRequestDTO("username", "password"))).thenReturn(new LoginResponseDTO("token"));
        LoginResponseDTO login = userServiceMock.login(new LoginRequestDTO("username", "password"));
        assertNotNull(login);
    }
    @Test
    void loginInvalid(){
        when(userServiceMock.login(new LoginRequestDTO("username", "password"))).thenReturn(null);
        LoginResponseDTO login = userServiceMock.login(new LoginRequestDTO("username", "password"));
        assertNull(login);
    }

    @Test
    void GetSumOfAllTransactionsFromAccountOfLast24Hours(){
        when(userServiceMock.GetSumOfAllTransactionsFromAccountOfLast24Hours(1L)).thenReturn(1000.0);
        int sum = (int) userServiceMock.GetSumOfAllTransactionsFromAccountOfLast24Hours(1L);
        assertEquals(1000, sum);
    }
    @Test
    void GetSumOfAllTransactionsFromAccountOfLast24HoursInvalid(){
        when(userServiceMock.GetSumOfAllTransactionsFromAccountOfLast24Hours(1L)).thenReturn(0.0);
        int sum = (int) userServiceMock.GetSumOfAllTransactionsFromAccountOfLast24Hours(1L);
        assertEquals(0, sum);
    }

    @Test
    void getRemainingDailyLimit(){
        when(userServiceMock.getRemainingDailyLimit(1L)).thenReturn(4000.0);
        int sum = (int) userServiceMock.getRemainingDailyLimit(1L);
        assertEquals(4000, sum);
    }
    @Test
    void getRemainingDailyLimitInvalid(){
        when(userServiceMock.getRemainingDailyLimit(1L)).thenReturn(0.0);
        int sum = (int) userServiceMock.getRemainingDailyLimit(1L);
        assertEquals(0, sum);
    }
}
