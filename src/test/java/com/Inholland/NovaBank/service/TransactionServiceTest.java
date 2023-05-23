package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountService accountService;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, accountRepository, userRepository, accountService);
    }

    @Test
    void getAll() {
        when(transactionRepository.findAll()).thenReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        List<Transaction> transactions = transactionService.GetAll();
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
    }

    @Test
    void getAllFromIban() {
        when(transactionRepository.findAllByFromAccountOrToAccount("NL01INHO0000000001", "NL01INHO0000000001")).thenReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        List<Transaction> transactions = transactionService.GetAllFromIban("NL01INHO0000000001");
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
    }


    @Test
    void add() {
        doReturn(new Transaction(LocalDateTime.now(), "NL01INHO0000000001", "NL01INHO0000000002", 100.0F, "test"))
            .when(transactionRepository)
            .save(Mockito.any(Transaction.class));

        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                new Account("NL01INHO0000000001", 1000, 1, AccountType.SAVINGS, true, 10)
        );
        when(accountRepository.findByIban("NL01INHO0000000002")).thenReturn(
                new Account("NL01INHO0000000002", 1000, 1, AccountType.SAVINGS, true, 10)
        );

        // Update stubbing to use doReturn().when() for stubbing with argument matching
        doReturn(new returnAccountDTO()).when(accountService).update(new patchAccountDTO("NL01INHO0000000001", "update", "balance", "900.0"));
        doReturn(new returnAccountDTO()).when(accountService).update(new patchAccountDTO("NL01INHO0000000002", "update", "balance", "1100.0"));

        Transaction transaction = transactionService.Add(new Transaction(LocalDateTime.now(), "NL01INHO0000000001", "NL01INHO0000000002", 100, "test"));
        assertNotNull(transaction);
        assertEquals("NL01INHO0000000001", transaction.getFromAccount());
        assertEquals("NL01INHO0000000002", transaction.getToAccount());
        assertEquals(100, transaction.getAmount());
        assertEquals("test", transaction.getDescription());
    }


    @Test
    void hasBalance() {
        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                new Account("NL01INHO0000000001", 1000, 1, AccountType.SAVINGS, true, 200)
        );
        boolean hasBalance = transactionService.HasBalance("NL01INHO0000000001", 100);
        assertTrue(hasBalance);

    }

    @Test
    void accountExists() {
        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                        new Account("NL01INHO0000000001", 1000, 1, AccountType.SAVINGS, true, 10)
        );
        when(accountRepository.findByIban("NL01INHO0000000002")).thenReturn(null);
        boolean accountExists = transactionService.AccountExists("NL01INHO0000000001");
        assertTrue(accountExists);
        accountExists = transactionService.AccountExists("NL01INHO0000000002");
        assertFalse(accountExists);
    }

    @Test
    void validateTransaction() {
        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                new Account("NL01INHO0000000001", 1000, 1, AccountType.SAVINGS, true, 10)
        );
        when(accountRepository.findByIban("NL01INHO0000000002")).thenReturn(
                new Account("NL01INHO0000000002", 1000, 1, AccountType.SAVINGS, true, 10)
        );
        when(userRepository.findUserTransactionLimitById(1L)).thenReturn(100);
        boolean validateTransaction = transactionService.ValidateTransaction(new Transaction(LocalDateTime.now(), "NL01INHO0000000001", "NL01INHO0000000002", 100, "test"));
        assertTrue(validateTransaction);
        validateTransaction = transactionService.ValidateTransaction(new Transaction(LocalDateTime.now(), "NL01INHO0000000001", "NL01INHO0000000002", 1000, "test"));
        assertFalse(validateTransaction);
        validateTransaction = transactionService.ValidateTransaction(new Transaction(LocalDateTime.now(), "NL01INHO0000000001", "NL01INHO0000000002", -100, "test"));
        assertFalse(validateTransaction);
    }

    @Test
    void getAllFromUser() {
        when(accountRepository.findAllIbansByUserReferenceId(1)).thenReturn(
                List.of(
                        "NL01INHO0000000001",
                        "NL01INHO0000000002"
                )
        );
        when(transactionRepository.findAllByFromAccountInOrToAccountIn(List.of("NL01INHO0000000001", "NL01INHO0000000002"), List.of("NL01INHO0000000001", "NL01INHO0000000002"))).thenReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )

        );
        List<Transaction> transaction = transactionService.GetAllFromUser(1);
        assertNotNull(transaction);
        assertEquals(2, transaction.size());

    }
}