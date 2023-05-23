package com.Inholland.NovaBank.service;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import com.Inholland.NovaBank.repositorie.TransactionRepository;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService extends BaseService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository, AccountService accountService){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    public List<Transaction> GetAll(){
        return (List<Transaction>) transactionRepository.findAll();
    }

    public List<Transaction> GetAllFromIban(String Iban){
        return transactionRepository.findAllByFromAccountOrToAccount(Iban, Iban);
    }

    public Transaction Add(Transaction transaction) {
        if (transaction.getDescription().length() == 0)
            transaction.setDescription("No description");
        Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
        Account toAccount = accountRepository.findByIban(transaction.getToAccount());
        if (fromAccount != null && toAccount != null)
        {
            accountService.update(new patchAccountDTO(fromAccount.getIban(), "update", "balance", Double.toString(fromAccount.getBalance() - transaction.getAmount())));
            accountService.update(new patchAccountDTO(toAccount.getIban(), "update", "balance", Double.toString(toAccount.getBalance() + transaction.getAmount())));
        }
        return transactionRepository.save(transaction);
    }

    public boolean HasBalance(String Iban, float amount){
        return accountRepository.findByIban(Iban).getBalance() >= amount;
    }

    public boolean AccountExists(String Iban){
        return accountRepository.findByIban(Iban) != null;
    }

    //checks if the account exists and if the account has enough balance to make the transaction and if the absolute limit is not exceeded
    public boolean ValidateTransaction(Transaction transaction){
        if (AccountExists(transaction.getFromAccount()) && AccountExists(transaction.getToAccount())){
            Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
            Account toAccount = accountRepository.findByIban(transaction.getToAccount());
            return HasBalance(transaction.getFromAccount(), transaction.getAmount())  && fromAccount.getAbsoluteLimit() <= fromAccount.getBalance() - transaction.getAmount() && userRepository.findUserTransactionLimitById(fromAccount.getUserReferenceId()) >= transaction.getAmount() && CheckForSavingsAccount(fromAccount, toAccount) && transaction.getAmount() > 0;
        }
        return false;
    }

    //first gets the transactions from the last 24 hours, then adds the amount of the new transaction to the total amount of the transactions from the last 24 hours and checks if it is less than the absolute limit
    private boolean CheckDailyLimit(String iban, double amount, float dailyLimit){
        List<Transaction> transactions = GetTransactionsFromLast24Hours(iban);
        float totalAmount = 0;
        for (Transaction transaction : transactions){
            totalAmount += transaction.getAmount();
        }
        return totalAmount + amount <= dailyLimit;
    }

    //gets a list of transactions from the last 24 hours
    private List<Transaction> GetTransactionsFromLast24Hours(String iban){
        return transactionRepository.findAllByFromAccountOrToAccountAndTimestampAfter(iban, iban, LocalDateTime.now().minusDays(1));
    }

    //check if the account is a savings account and if it is, check if the user reference id is the same
    private boolean CheckForSavingsAccount(Account fromAccount, Account toAccount){
        if (fromAccount.getAccountType() == AccountType.SAVINGS || toAccount.getAccountType() == AccountType.SAVINGS) {
            return fromAccount.getUserReferenceId() == toAccount.getUserReferenceId();
        }
        return CheckDailyLimit(fromAccount.getIban(), fromAccount.getBalance(), userRepository.findUserDailyLimitById(fromAccount.getUserReferenceId()));
    }

    public List<Transaction> GetAllFromUser(long userId){
        List<String> ibans = accountRepository.findAllIbansByUserReferenceId(userId);
        return transactionRepository.findAllByFromAccountInOrToAccountIn(ibans, ibans);
    }
}
