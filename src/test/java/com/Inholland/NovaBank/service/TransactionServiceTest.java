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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
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

    @MockBean
    private TransactionService transactionServiceMock;

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
        List<TransactionResponceDTO> transactions = transactionService.GetAllFromIban("NL01INHO0000000001");
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

        TransactionResponceDTO transaction = transactionService.Add(new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test"));
        assertNotNull(transaction);
        assertEquals("NL01INHO0000000001", transaction.getFromAccount());
        assertEquals("NL01INHO0000000002", transaction.getToAccount());
        assertEquals(100, transaction.getAmount());
        assertEquals("test", transaction.getDescription());
    }

    @Test
    void add2(){
        when(transactionServiceMock.Add(new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test"))).thenReturn(
                new TransactionResponceDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test", LocalDateTime.now())
        );
        TransactionResponceDTO transaction = transactionServiceMock.Add(new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test"));
        assertNotNull(transaction);
        assertEquals("NL01INHO0000000001", transaction.getFromAccount());
        assertEquals("NL01INHO0000000002", transaction.getToAccount());
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
        when(userRepository.findUserDayLimitById(1l)).thenReturn(1000);
        boolean validateTransaction = transactionService.ValidateTransaction(new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 100, "test"));
        assertTrue(validateTransaction);
        validateTransaction = transactionService.ValidateTransaction(new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", 1000, "test"));
        assertFalse(validateTransaction);
        validateTransaction = transactionService.ValidateTransaction(new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", -100, "test"));
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
        List<TransactionResponceDTO> transaction = transactionService.GetAllFromUser(1);
        assertNotNull(transaction);
        assertEquals(2, transaction.size());

    }


    @Test
    void getTransactionsFromLast24HoursByUser() {
        when(accountRepository.findAllIbansByUserReferenceId(1)).thenReturn(
                List.of(
                        "NL01INHO0000000001",
                        "NL01INHO0000000002"
                )
        );
        when(transactionRepository.findAllByFromAccountAndTimestampAfterAndFromAccountNotInOrToAccountNotIn(any(String.class), any(LocalDateTime.class), any(List.class), any(List.class))).thenReturn(
                List.of(
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "NL01INHO0000000002", 100, "test"),
                        new Transaction(LocalDateTime.now(),"NL01INHO0000000002", "NL01INHO0000000001", 100, "test")
                )
        );
        List<Transaction> transactions = transactionService.GetTransactionsFromLast24HoursByUser(1);
        assertEquals(2, transactions.size());
        assertEquals("NL01INHO0000000001", transactions.get(0).getFromAccount());

    }


    @Test
    void validateWithdraw() {
        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                new Account("NL01INHO0000000001", 1000, 1, AccountType.SAVINGS, true, 10)
        );
        boolean validateWithdraw = transactionService.ValidateWithdraw(new DepositWithdrawDTO("NL01INHO0000000001", 100));
        assertTrue(validateWithdraw);
    }

    @Test
    void withdraw() {
        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                new Account("NL01INHO0000000001", 1000, 1, AccountType.SAVINGS, true, 10)
        );

        when(accountService.update(Mockito.any(patchAccountDTO.class))).thenReturn(
                new returnAccountDTO("NL01INHO0000000001", AccountType.CHECKING)
        );
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(
                new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "withdraw", 100, "withdraw")
        );
        transactionService.withdraw(new DepositWithdrawDTO("NL01INHO0000000001", 100));
        Transaction transaction = transactionService.withdraw(new DepositWithdrawDTO("NL01INHO0000000001", 100));


        assertNotNull(transaction);
        assertEquals("NL01INHO0000000001", transaction.getFromAccount());
        assertEquals("withdraw", transaction.getToAccount());
        assertEquals(100, transaction.getAmount());
        assertEquals("withdraw", transaction.getDescription());
    }

    @Test
    void withdraw2(){
        when(transactionServiceMock.withdraw(new DepositWithdrawDTO("NL01INHO0000000001", 100))).thenReturn(
                new Transaction(LocalDateTime.now(),"NL01INHO0000000001", "withdraw", 100, "withdraw")
        );
        Transaction transaction = transactionServiceMock.withdraw(new DepositWithdrawDTO("NL01INHO0000000001", 100));
        assertNotNull(transaction);
        assertEquals("NL01INHO0000000001", transaction.getFromAccount());
        assertEquals("withdraw", transaction.getToAccount());
    }


    @Test
    void validateDeposit() {
        assertTrue(transactionService.ValidateDeposit(new DepositWithdrawDTO("NL01INHO0000000001", 100)));
        assertFalse(transactionService.ValidateDeposit(new DepositWithdrawDTO("NL01INHO0000000001", -100)));
    }

    @Test
    void deposit() {
        when(accountRepository.findByIban("NL01INHO0000000001")).thenReturn(
                new Account("NL01INHO0000000001", 1000, 1, AccountType.SAVINGS, true, 10)
        );

        when(accountService.update(Mockito.any(patchAccountDTO.class))).thenReturn(
                new returnAccountDTO("NL01INHO0000000001", AccountType.CHECKING)
        );
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(
                new Transaction(LocalDateTime.now(),"deposit", "NL01INHO0000000001", 100, "deposit")
        );
        transactionService.deposit(new DepositWithdrawDTO("NL01INHO0000000001", 100));
        Transaction transaction = transactionService.deposit(new DepositWithdrawDTO("NL01INHO0000000001", 100));

        assertNotNull(transaction);
        assertEquals("deposit", transaction.getFromAccount());
        assertEquals("NL01INHO0000000001", transaction.getToAccount());
        assertEquals(100, transaction.getAmount());
        assertEquals("deposit", transaction.getDescription());
    }

    @Test
    void deposit2(){
        when(transactionServiceMock.deposit(new DepositWithdrawDTO("NL01INHO0000000001", 100))).thenReturn(
                new Transaction(LocalDateTime.now(),"deposit", "NL01INHO0000000001", 100, "deposit")
        );
        Transaction transaction = transactionServiceMock.deposit(new DepositWithdrawDTO("NL01INHO0000000001", 100));
        assertNotNull(transaction);
        assertEquals("deposit", transaction.getFromAccount());
        assertEquals("NL01INHO0000000001", transaction.getToAccount());
    }
}