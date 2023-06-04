package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AccountController;
import com.Inholland.NovaBank.model.*;
import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import com.Inholland.NovaBank.repositorie.UserRepository;
import com.Inholland.NovaBank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private JwtTokenProvider jwttokenprovider;


    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {

        userService = new UserService(userRepository,bCryptPasswordEncoder, jwttokenprovider);
        accountService = new AccountService(accountRepository, userRepository, userService);

    }

    @Test
    void getAll() {

                when(accountRepository.findAllAccounts(accountService.getPageable(2L,0L))).thenReturn(
                List.of(
                        new Account("NL01INHO0000000001", 200,1, AccountType.SAVINGS,true,200),
                        new Account("NL01INHO0000000002", 200,1, AccountType.SAVINGS,true,200)
                )
        );
        List<Account> accounts = accountService.getAll(2L,0L);
        assertNotNull(accounts);
        assertEquals(2, accounts.size());

    }

    @Test
    void getAllActive() {
        when(accountRepository.findAllAccountsActive(accountService.getPageable(2L,0L),true)).thenReturn(
                List.of(
                        new Account("NL01INHO0000000001", 200,1, AccountType.SAVINGS,true,200),
                        new Account("NL01INHO0000000002", 200,1, AccountType.SAVINGS,true,200)
                )
        );
        List<Account> accounts = accountService.getAllActive(2L,0L,true);
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
    }

    @Test
    void testGetAll() {
        when(accountRepository.findAllAccounts(accountService.getPageable(2L,0L))).thenReturn(
                List.of(
                        new Account("NL01INHO0000000001", 200,1, AccountType.SAVINGS,true,200),
                        new Account("NL01INHO0000000002", 200,1, AccountType.SAVINGS,true,200)
                )
        );
        List<Account> accounts = accountService.getAll(2L,0L);
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
    }

    @Test
    void getByUserId() {
        when(accountRepository.findByuserReferenceId(2)).thenReturn(
                List.of(
                        new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200),
                        new Account("NL01INHO0000000002", 200,2, AccountType.SAVINGS,true,200)
                )
        );
        Authentication authentication = Mockito.mock(Authentication.class);
        when(userService.getUserByUsername("henk")).thenReturn(new User("henk", "tarp", "henk", "1234", "henk", Role.ROLE_ADMIN, 200, 200, true));
        when(authentication.getName()).thenReturn("henk");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);;
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        List<Account> accounts = accountService.getByUserId(2);
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
    }
    @Test
    void getByUserIdInvalid() {
        when(accountRepository.findByuserReferenceId(2)).thenReturn(
                null
        );
        Authentication authentication = Mockito.mock(Authentication.class);
        when(userService.getUserByUsername("henk")).thenReturn(new User("henk", "tarp", "henk", "1234", "henk", Role.ROLE_ADMIN, 200, 200, true));
        when(authentication.getName()).thenReturn("henk");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);;
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        List<Account> accounts = accountService.getByUserId(2);
        //check if null
        assertNull(accounts);


    }





    @Test
    void checkLimit(){
        assertTrue(accountService.checkLimit(200));

    }

    @Test
    void checkLimitFalse(){
        assertFalse(accountService.checkLimit(-200));

    }

    @Test
    void checkLimitFalse2(){
        assertTrue(accountService.checkLimit(0));

    }

    @Test
    void checkLimitFalse3(){
        assertFalse(accountService.checkLimit(1000000000));

    }




    @Test
    void add() {
        doReturn(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200))
                .when(accountRepository)
                .save(Mockito.any(Account.class));
        User user = new User("henk","tarp","henk","1234","henk@gmail.com", Role.ROLE_ADMIN,200,200,true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        returnAccountDTO account = accountService.add(new newAccountDTO(2, AccountType.SAVINGS,200));
        assertNotNull(account);
        assertEquals("NL01INHO0000000001", account.getIban());
    }

    @Test
    void update() {
        when(accountRepository.save(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,250))).thenReturn(
                new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,250)
        );
        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200)
        );
        returnAccountDTO account = accountService.update(new patchAccountDTO("NL01INHO0000000001","update","absoluteLimit","250"));
        assertNotNull(account);
        assertEquals("NL01INHO0000000001", account.getIban());

    }
}
