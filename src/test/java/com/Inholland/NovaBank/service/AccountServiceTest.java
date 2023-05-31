package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AccountController;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.model.Transaction;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {

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

        List<Account> accounts = accountService.getByUserId(2);
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
    }

    void authUser(){



    }

    @Test
    void add() {
        doReturn(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200))
                .when(accountRepository)
                .save(Mockito.any(Account.class));


        returnAccountDTO account = accountService.add(new newAccountDTO(2, AccountType.SAVINGS,200));
        assertNotNull(account);
        assertEquals("NL01INHO0000000001", account.getIban());
    }

    @Test
    void update() {
        when(accountRepository.save(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,250))).thenReturn(
                new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,250)
        );
        returnAccountDTO account = accountService.update(new patchAccountDTO("NL01INHO0000000001","update","absoluteLimit","250"));
        assertNotNull(account);
        assertEquals("NL01INHO0000000001", account.getIban());

    }
}
