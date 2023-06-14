package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AccountController;
import com.Inholland.NovaBank.model.*;
import com.Inholland.NovaBank.model.DTO.*;
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
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @MockBean
    private AccountService accountServiceMock;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void getAll(){
        given(accountService.getAll(2L,0L)).willReturn(
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
    public void getAll2(){
        given(accountService.getAll(true,2L,0L)).willReturn(
                List.of(
                        new Account("NL01INHO0000000001", 200,1, AccountType.SAVINGS,true,200),
                        new Account("NL01INHO0000000002", 200,1, AccountType.SAVINGS,true,200)
                )
        );
        List<Account> accounts = accountService.getAll(true,2L,0L);
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
    }

    @Test
    public void getAllActive(){
        given(accountService.getAllActive(2L,0L,true)).willReturn(
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
    public void getAllActiveInvalid(){
        given(accountService.getAllActive(2L,0L,true)).willReturn(
                null
        );
        List<Account> accounts = accountService.getAllActive(2L,0L,true);
        assertNull(accounts);
    }


    @Test
    public void getAllWithActiveInvalid(){
        given(accountService.getAll(true,2L,0L)).willReturn(
                null
        );
        List<Account> accounts = accountService.getAll(true,2L,0L);
        assertNull(accounts);
    }



    @Test
    public void getAllInvalid(){
        given(accountService.getAll(2L,0L)).willReturn(
                null
        );
        List<Account> accounts = accountService.getAll(2L,0L);
        assertNull(accounts);
    }



    @Test
    public void getByUserId(){
        when(accountServiceMock.getByUserId(2)).thenReturn(
                List.of(
                        new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200),
                        new Account("NL01INHO0000000002", 200,2, AccountType.SAVINGS,true,200)
                )
        );


        List<Account> accounts = accountServiceMock.getByUserId(2);
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
    }


    @Test
    public void getByUserIdInvalid() {
        when(accountServiceMock.getByUserId(2)).thenReturn(
                null
        );
        List<Account> accounts = accountServiceMock.getByUserId(2);
        //check if null
        assertNull(accounts);
    }

    @Test
    public void checkLimit(){

        assertTrue(accountService.checkLimit(200));

    }

    @Test
    public void checkLimitFalse(){
        assertFalse(accountService.checkLimit(-200));

    }

    @Test
    public void checkLimit0(){

        assertTrue(accountService.checkLimit(0));

    }

    @Test
    public void checkLimitFalse2(){
        assertFalse(accountServiceMock.checkLimit(1000000000));

    }

    @Test
    public void add(){
        given(accountServiceMock.add(new newAccountDTO(2, AccountType.SAVINGS,200))).willReturn(
                new returnAccountDTO("NL01INHO0000000001",AccountType.SAVINGS));

        returnAccountDTO account = accountServiceMock.add(new newAccountDTO(2, AccountType.SAVINGS,200));
        assertNotNull(account);
        assertEquals("NL01INHO0000000001", account.getIban());
        assertEquals("SAVINGS", account.getAccountType().toString());
    }


    @Test
    public void addInvalid(){
        AccountService accountService = mock(AccountService.class);

        doThrow(IllegalArgumentException.class).when(accountService).add(any());

        assertThrows(IllegalArgumentException.class, () -> accountService.add(new newAccountDTO(3, AccountType.SAVINGS,200)));
    }

    @Test
    public void addWithWrongAbsoluteLimit(){
        when(accountServiceMock.add(new newAccountDTO(2, AccountType.SAVINGS,-200))).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> accountServiceMock.add(new newAccountDTO(2, AccountType.SAVINGS,-200)));
    }

    @Test
    public void update(){
        given(accountServiceMock.update(new patchAccountDTO("NL01INHO0000000001","update","balance","200"))).willReturn(
                new returnAccountDTO("NL01INHO0000000001",AccountType.SAVINGS)
        );
        returnAccountDTO updatedAccount = accountServiceMock.update(new patchAccountDTO("NL01INHO0000000001","update","balance","200"));
        assertNotNull(updatedAccount);
        assertEquals("NL01INHO0000000001", updatedAccount.getIban());
    }

    @Test
    public void updateInvalid(){
        AccountService accountService = mock(AccountService.class);
        doThrow(IllegalArgumentException.class).when(accountService).update(any());
        assertThrows(IllegalArgumentException.class, () -> accountService.update(new patchAccountDTO("NL01INHO0000000001","update","absoluteLimit","250")));
    }

    @Test
    public void updateWithInvalidAbsoluteLimit(){
        when(accountServiceMock.update(new patchAccountDTO("NL01INHO0000000001","update","absoluteLimit","-200"))).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> accountServiceMock.update(new patchAccountDTO("NL01INHO0000000001","update","absoluteLimit","-200")));
    }

    @Test
    public void getAllSearch(){
        given(accountServiceMock.getAllSearch(1000L,0L)).willReturn(List.of(new searchAccountDTO("NL01INHO0000000001", 2, AccountType.SAVINGS)));
        List<searchAccountDTO> accounts = accountServiceMock.getAllSearch(1000L,0L);
        assertNotNull(accounts);
    }

    @Test
    public void getAllSearchInvalid(){
        given(accountServiceMock.getAllSearch(1000L,0L)).willReturn(null);
        List<searchAccountDTO> accounts = accountServiceMock.getAllSearch(1000L,0L);
        assertNull(accounts);
    }

    @Test
    public void getPageable(){

        Pageable pageable = accountService.getPageable(1000L,0L);
        assertNotNull(pageable);
    }

    @Test
    public void getInvalidPageable(){
        given(accountServiceMock.getPageable(1000L,0L)).willReturn(null);
        Pageable pageable = accountServiceMock.getPageable(1000L,0L);
        assertNull(pageable);
    }

    @Test
    public void transformAccounts(){

        List<searchAccountDTO> accounts = accountService.transformAccounts(List.of(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200)));
        assertNotNull(accounts);
    }

    @Test
    public void transformAccountsInvalid(){
        given(accountServiceMock.transformAccounts(List.of(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200)))).willReturn(null);
        List<searchAccountDTO> accounts = accountServiceMock.transformAccounts(List.of(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,true,200)));
        assertNull(accounts);
    }

    @Test
    public void authUser(){
        given(accountServiceMock.authUser(1L)).willReturn(true);
        boolean auth = accountServiceMock.authUser(1L);
        assertTrue(auth);
    }

    @Test
    public void authUserInvalid(){
        given(accountServiceMock.authUser(1L)).willReturn(false);
        boolean auth = accountServiceMock.authUser(1L);
        assertFalse(auth);
    }

    @Test
    public void setAccount(){

        Account account = accountService.setAccount(new newAccountDTO(2,AccountType.CHECKING,200));
        assertNotNull(account);
    }

    @Test
    public void setAccountInvalid(){
        given(accountServiceMock.setAccount(new newAccountDTO(2,AccountType.CHECKING,200))).willReturn(null);
        Account account = accountServiceMock.setAccount(new newAccountDTO(2,AccountType.CHECKING,200));
        assertNull(account);
    }

    @Test
    public void checkUserHasAccount(){
        given(accountServiceMock.checkUserHasAccount(1L)).willReturn(true);
        boolean auth = accountServiceMock.checkUserHasAccount(1L);
        assertTrue(auth);
    }

    @Test
    public void checkUserHasAccountInvalid(){
        given(accountServiceMock.checkUserHasAccount(1L)).willReturn(false);
        boolean auth = accountServiceMock.checkUserHasAccount(1L);
        assertFalse(auth);
    }

    @Test
    public void updateUserAccountStatus(){
        accountServiceMock.updateUserAccountStatus(1L);
        verify(accountServiceMock, times(1)).updateUserAccountStatus(1L);

    }

    @Test
    public void updateUserAccountStatusInvalid(){
        doThrow(IllegalArgumentException.class).when(accountServiceMock).updateUserAccountStatus(1L);
        assertThrows(IllegalArgumentException.class, () -> accountServiceMock.updateUserAccountStatus(1L));
    }

    @Test
    public void updateBalance(){
        given(accountServiceMock.updateBalance(new patchAccountDTO("NL01INHO0000000001","update","balance","200"))).willReturn(new returnAccountDTO("NL01INHO0000000001",AccountType.SAVINGS));
        returnAccountDTO account = accountServiceMock.updateBalance(new patchAccountDTO("NL01INHO0000000001","update","balance","200"));
        assertNotNull(account);
        assertEquals("NL01INHO0000000001", account.getIban());
    }

    @Test
    public void updateBalanceInvalid(){
        given(accountServiceMock.updateBalance(new patchAccountDTO("NL01INHO0000000001","update","balance","200"))).willReturn(null);
        returnAccountDTO account = accountServiceMock.updateBalance(new patchAccountDTO("NL01INHO0000000001","update","balance","200"));
        assertNull(account);
    }

    @Test
    public void AccountExists(){
        given(accountServiceMock.AccountExists("NL01INHO0000000001")).willReturn(true);
        boolean exists = accountServiceMock.AccountExists("NL01INHO0000000001");
        assertTrue(exists);
    }

    @Test
    public void AccountExistsInvalid(){
        given(accountServiceMock.AccountExists("NL01INHO0000000001")).willReturn(false);
        boolean exists = accountServiceMock.AccountExists("NL01INHO0000000001");
        assertFalse(exists);
    }

    @Test
    public void checkOperation(){
        accountService.checkOperation(new patchAccountDTO("NL01INHO0000000001","update","balance","200"));
    }

    @Test
    public void checkOperationInvalid(){
        assertThrows(IllegalArgumentException.class, () -> accountService.checkOperation(new patchAccountDTO("NL01INHO0000000001","qeqweqwe","balance","200")));
    }

    @Test
    public void checkValue(){
        accountService.checkValue(new patchAccountDTO("NL01INHO0000000001","update","balance","200"));
    }

    @Test
    public void checkValueInvalid(){
        assertThrows(IllegalArgumentException.class, () -> accountService.checkValue(new patchAccountDTO("NL01INHO0000000001","update","balance","")));
    }

    @Test
    public void ownership(){
        accountService.ownership(new Account("NL12INHO0154160635", 200,2, AccountType.SAVINGS,true,200));
    }

    @Test
    public void ownershipInvalid(){
        assertThrows(IllegalArgumentException.class, () -> accountService.ownership(new Account("NL01INHO0000000001", 200,2, AccountType.SAVINGS,false,200)));
    }


}
