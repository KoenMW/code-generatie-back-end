package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import com.Inholland.NovaBank.repositorie.TransactionRepository;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AccountService accountService;

    private TransactionService transactionService;



    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, accountRepository, userRepository, accountService);
    }

    @Test
    void getAllNotNull() {
        given(transactionRepository.findAll()).willReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 100, 1L, AccountType.CHECKING, true, 100)
        );
        given(accountRepository.findByIban("NL01INHO0000000002")).willReturn(
                new Account("NL01INHO0000000002", 100, 1L, AccountType.CHECKING, true, 100)
        );
        List<TransactionResponceDTO> transactions = transactionService.GetAll();
        assertNotNull(transactions);
    }

    @Test
    void getAllSize() {
        given(transactionRepository.findAll()).willReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 100, 1L, AccountType.CHECKING, true, 100)
        );
        given(accountRepository.findByIban("NL01INHO0000000002")).willReturn(
                new Account("NL01INHO0000000002", 100, 1L, AccountType.CHECKING, true, 100)
        );
        List<TransactionResponceDTO> transactions = transactionService.GetAll();
        assertEquals(2, transactions.size());
    }

    @Test
    void getAllFromIbanNotNull() {
        given(transactionRepository.findAllByFromAccountOrToAccount("NL01INHO0000000001", "NL01INHO0000000001")).willReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 100, 1L, AccountType.CHECKING, true, 100)
        );
        given(accountRepository.findByIban("NL01INHO0000000002")).willReturn(
                new Account("NL01INHO0000000002", 100, 1L, AccountType.CHECKING, true, 100)
        );
        List<TransactionResponceDTO> transactions = transactionService.GetAllFromIban("NL01INHO0000000001");
        assertNotNull(transactions);
    }


    @Test
    void getAllFromIbanSize() {
        given(transactionRepository.findAllByFromAccountOrToAccount("NL01INHO0000000001", "NL01INHO0000000001")).willReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 100, 1L, AccountType.CHECKING, true, 100)
        );
        given(accountRepository.findByIban("NL01INHO0000000002")).willReturn(
                new Account("NL01INHO0000000002", 100, 1L, AccountType.CHECKING, true, 100)
        );
        List<TransactionResponceDTO> transactions = transactionService.GetAllFromIban("NL01INHO0000000001");
        assertEquals(2, transactions.size());
    }

    @Test
    void addNotNull() {
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 100, 1L, AccountType.CHECKING, true, 100)
        );
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test");
        Transaction transaction = new Transaction(LocalDateTime.now(), "NL01INHO0000000001", "NL01INHO0000000002", 100, "test");
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);
        TransactionResponceDTO transactionResponceDTO = transactionService.Add(transactionRequestDTO);
        assertNotNull(transactionResponceDTO);
    }

    @Test
    void hasBalanceTrue() {
        Account account = new Account("NL01INHO0000000001", 300, 1l, AccountType.CREDIT, true, 100);
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(account);
        boolean hasBalance = transactionService.HasBalance("NL01INHO0000000001", 100);
        assertTrue(hasBalance);
    }

    @Test
    void hasBalanceFalse(){
        Account account = new Account("NL01INHO0000000001", 300, 1l, AccountType.CREDIT, true, 100);
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(account);
        boolean hasBalance = transactionService.HasBalance("NL01INHO0000000001", 400);
        assertFalse(hasBalance);
    }

    @Test
    void validateTransactionTrue() {
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test");
        given(accountService.AccountExists("NL01INHO0000000001")).willReturn(true);
        given(accountService.AccountExists("NL01INHO0000000002")).willReturn(true);
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 1000, 1L, AccountType.CHECKING, true, 100)
        );
        given(accountRepository.findByIban("NL01INHO0000000002")).willReturn(
                new Account("NL01INHO0000000002", 1000, 1L, AccountType.CHECKING, true, 100)
        );
        given(userRepository.findUserDayLimitById(1L)).willReturn(100);
        boolean validate = transactionService.ValidateTransaction(transactionRequestDTO);
        System.out.println(validate);
        assertTrue(validate);
    }

    @Test
    void validateTransactionFalse(){
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test");
        given(accountService.AccountExists("NL01INHO0000000001")).willReturn(true);
        given(accountService.AccountExists("NL01INHO0000000002")).willReturn(false);

        boolean validate = transactionService.ValidateTransaction(transactionRequestDTO);
        assertFalse(validate);
    }

    @Test
    void getAllFromUserNotNull() {
        given(accountRepository.findAllIbansByUserReferenceId(1L)).willReturn(
                List.of(
                        "NL01INHO0000000001",
                        "NL01INHO0000000002"
                )
        );
        given(transactionRepository.findAllByFromAccountInOrToAccountIn(List.of("NL01INHO0000000001", "NL01INHO0000000002"), List.of("NL01INHO0000000001", "NL01INHO0000000002"))).willReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 100, 1L, AccountType.CHECKING, true, 100)
        );
        given(accountRepository.findByIban("NL01INHO0000000002")).willReturn(
                new Account("NL01INHO0000000002", 100, 1L, AccountType.CHECKING, true, 100)
        );
        List<TransactionResponceDTO> transactions = transactionService.GetAllFromUser(1L);
        assertNotNull(transactions);
    }

    @Test
    void getAllFromUserSize(){
        given(accountRepository.findAllIbansByUserReferenceId(1L)).willReturn(
                List.of(
                        "NL01INHO0000000001",
                        "NL01INHO0000000002"
                )
        );
        given(transactionRepository.findAllByFromAccountInOrToAccountIn(List.of("NL01INHO0000000001", "NL01INHO0000000002"), List.of("NL01INHO0000000001", "NL01INHO0000000002"))).willReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 300, 1L, AccountType.CREDIT, true, 100)
        );
        given(accountRepository.findByIban("NL01INHO0000000002")).willReturn(
                new Account("NL01INHO0000000002", 300, 1L, AccountType.CREDIT, true, 100)
        );
        List<TransactionResponceDTO> transactions = transactionService.GetAllFromUser(1L);
        assertEquals(2, transactions.size());
    }

    @Test
    void validateWithdrawTrue() {
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 300, 1L, AccountType.CREDIT, true, 100)
        );
        given(userRepository.findUserDayLimitById(1L)).willReturn(100);
        boolean validate = transactionService.ValidateWithdraw(new DepositWithdrawDTO("NL01INHO0000000001", 100));
        assertTrue(validate);
    }

    @Test
    void validateWithdrawFalse(){
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 300, 1L, AccountType.CREDIT, true, 100)
        );
        given(userRepository.findUserDayLimitById(1L)).willReturn(100);
        boolean validate = transactionService.ValidateWithdraw(new DepositWithdrawDTO("NL01INHO0000000001", 200));
        assertFalse(validate);
    }

    @Test
    void withdrawNotNull() {
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 300, 1L, AccountType.CREDIT, true, 100)
        );
        given(accountService.updateBalance(new patchAccountDTO("NL01INHO0000000001", "update", "balance", "100"))).willReturn(
                new returnAccountDTO("NL01INHO0000000001",AccountType.CREDIT)
        );
        given(transactionRepository.save(new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "withdraw", 200, "Withdraw"))).willReturn(
                new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000001", 200, "withdraw")
        );
        TransactionResponceDTO transaction = transactionService.Withdraw(new DepositWithdrawDTO("NL01INHO0000000001", 200));
        assertNotNull(transaction);
    }

    @Test
    void validateDepositTrue() {
        assertTrue(transactionService.ValidateDeposit(new DepositWithdrawDTO("NL01INHO0000000001", 100)));
    }


    @Test
    void validateDepositzeroIsFalse() {
        assertFalse(transactionService.ValidateDeposit(new DepositWithdrawDTO("NL01INHO0000000001", 0)));
    }
    
    @Test
    void validateDepositMoreThanOneThousandFalse() {
        assertFalse(transactionService.ValidateDeposit(new DepositWithdrawDTO("NL01INHO0000000001", 1001)));
    }

    @Test
    void validateDepositLessThanZeroTrue() {
        assertFalse(transactionService.ValidateDeposit(new DepositWithdrawDTO("NL01INHO0000000001", -1)));
    }

    @Test
    void depositNotNull() {
        given(accountRepository.findByIban("NL01INHO0000000001")).willReturn(
                new Account("NL01INHO0000000001", 300, 1L, AccountType.CREDIT, true, 100)
        );
        given(accountService.updateBalance(new patchAccountDTO("NL01INHO0000000001", "update", "balance", "500.0"))).willReturn(
                new returnAccountDTO("NL01INHO0000000001",AccountType.CREDIT)
        );
        given(transactionRepository.save(any(Transaction.class))).willReturn(
                new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000001", 200, "deposit")
        );
        TransactionResponceDTO transaction = transactionService.Deposit(new DepositWithdrawDTO("NL01INHO0000000001", 200));

        assertNotNull(transaction);
    }
}