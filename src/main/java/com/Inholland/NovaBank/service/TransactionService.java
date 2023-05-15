package com.Inholland.NovaBank.service;

        import com.Inholland.NovaBank.model.Account;
        import com.Inholland.NovaBank.model.AccountType;
        import com.Inholland.NovaBank.model.Transaction;
        import com.Inholland.NovaBank.repositorie.AccountRepository;
        import com.Inholland.NovaBank.repositorie.TransactionRepositorie;
        import jakarta.transaction.Transactional;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.time.LocalDateTime;
        import java.util.List;

@Service
public class TransactionService extends BaseService {

    @Autowired
    private TransactionRepositorie transactionRepositorie;

    @Autowired
    private AccountRepository accountRepository;

    public List<Transaction> GetAll(){
        return (List<Transaction>) transactionRepositorie.findAll();
    }

    public List<Transaction> GetAllFromIban(String Iban){
        return transactionRepositorie.findAllByFromAccountOrToAccount(Iban, Iban);
    }

    public void Add(Transaction transaction) {
        if (transaction.getDescription().length() == 0)
            transaction.setDescription("No description");
        Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
        Account toAccount = accountRepository.findByIban(transaction.getToAccount());
        if (fromAccount != null && toAccount != null)
        {
            accountRepository.setBalance(fromAccount.getIban(), fromAccount.getBalance() - transaction.getAmount());
            accountRepository.setBalance(toAccount.getIban(), toAccount.getBalance() + transaction.getAmount());
        }
        transactionRepositorie.save(transaction);
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
            return HasBalance(transaction.getFromAccount(), transaction.getAmount()) && CheckDailyLimit(fromAccount, transaction.getAmount())  && fromAccount.getAbsoluteLimit() <= fromAccount.getBalance() - transaction.getAmount() && fromAccount.getTransactionLimit() >= transaction.getAmount() && CheckForSavingsAccount(fromAccount, toAccount);
        }
        return false;
    }

    //first gets the transactions from the last 24 hours, then adds the amount of the new transaction to the total amount of the transactions from the last 24 hours and checks if it is less than the absolute limit
    private boolean CheckDailyLimit(Account account, float amount){
        List<Transaction> transactions = GetTransactionsFromLast24Hours(account.getIban());
        float totalAmount = 0;
        for (Transaction transaction : transactions){
            totalAmount += transaction.getAmount();
        }
        return totalAmount + amount <= account.getDailyLimit();
    }

    //gets a list of transactions from the last 24 hours
    private List<Transaction> GetTransactionsFromLast24Hours(String iban){
        return transactionRepositorie.findAllByFromAccountOrToAccountAndTimestampAfter(iban, iban, LocalDateTime.now().minusDays(1));
    }

    //check if the account is a savings account and if it is, check if the user reference id is the same
    private boolean CheckForSavingsAccount(Account fromAccount, Account toAccount){
        if (fromAccount.getAccountType() == AccountType.SAVINGS || toAccount.getAccountType() == AccountType.SAVINGS) {
            return fromAccount.getUserReferenceId() == toAccount.getUserReferenceId();
        }
        return true;
    }
}
