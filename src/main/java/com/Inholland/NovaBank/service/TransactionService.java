package com.Inholland.NovaBank.service;

        import com.Inholland.NovaBank.model.Account;
        import com.Inholland.NovaBank.model.Transaction;
        import com.Inholland.NovaBank.repositorie.AccountRepository;
        import com.Inholland.NovaBank.repositorie.TransactionRepositorie;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

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
            System.out.println("From: " + fromAccount.getIban() + " To: " + toAccount.getIban());
            accountRepository.setBalance(fromAccount.getIban(), fromAccount.getBalance() - transaction.getAmount());
            accountRepository.setBalance(toAccount.getIban(), toAccount.getBalance() + transaction.getAmount());
        }
        transactionRepositorie.save(transaction);
    }

    public boolean HasBalance(String Iban, float amount){
        return true;//accountRepository.getBalance(Iban) >= amount;
    }
}
