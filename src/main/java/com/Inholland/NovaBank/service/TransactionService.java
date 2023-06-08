package com.Inholland.NovaBank.service;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.DepositWithdrawDTO;
import com.Inholland.NovaBank.model.DTO.TransactionRequestDTO;
import com.Inholland.NovaBank.model.DTO.TransactionResponceDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import com.Inholland.NovaBank.repositorie.TransactionRepository;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<TransactionResponceDTO> GetAllFromIban(String Iban){
        return ConvertToResponce(transactionRepository.findAllByFromAccountOrToAccount(Iban, Iban));
    }

    public TransactionResponceDTO Add(TransactionRequestDTO transaction) {
        if (transaction.getDescription().length() == 0)
            transaction.setDescription("No description");
        Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
        Account toAccount = accountRepository.findByIban(transaction.getToAccount());
        if (fromAccount != null && toAccount != null)
        {
            accountService.update(new patchAccountDTO(fromAccount.getIban(), "update", "balance", Double.toString(fromAccount.getBalance() - transaction.getAmount())));
            accountService.update(new patchAccountDTO(toAccount.getIban(), "update", "balance", Double.toString(toAccount.getBalance() + transaction.getAmount())));
        }
        Transaction saved = transactionRepository.save(new Transaction(LocalDateTime.now(), transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount(), transaction.getDescription()));
        return ConvertToResponce(saved);
    }

    public boolean HasBalance(String Iban, float amount){
        return accountRepository.findByIban(Iban).getBalance() >= amount;
    }

    private String GetDirection(String FromIban, Long userid){
        List<String> ibans = accountRepository.findAllIbansByUserReferenceId(userid);
        if (ibans.contains(FromIban))
            return "-";
        return "+";
    }

    public boolean AccountExists(String Iban){
        return accountRepository.findByIban(Iban) != null;
    }

    //checks if the account exists and if the account has enough balance to make the transaction and if the absolute limit is not exceeded
    public boolean ValidateTransaction(TransactionRequestDTO transaction){
        if (AccountExists(transaction.getFromAccount()) && AccountExists(transaction.getToAccount())){
            Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
            Account toAccount = accountRepository.findByIban(transaction.getToAccount());
            return HasBalance(transaction.getFromAccount(), transaction.getAmount())  && fromAccount.getAbsoluteLimit() <= fromAccount.getBalance() - transaction.getAmount() && CheckDailyLimit(transaction.getFromAccount(), transaction.getAmount(), userRepository.findUserDayLimitById(fromAccount.getUserReferenceId())) && CheckForSavingsAccount(fromAccount, toAccount) && transaction.getAmount() > 0;
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

    public List<Transaction> GetTransactionsFromLast24HoursByUser(long userId){
        List<String> ibans = accountRepository.findAllIbansByUserReferenceId(userId);
        return transactionRepository.findAllByFromAccountAndTimestampAfterAndFromAccountNotInOrToAccountNotIn(ibans.get(0), LocalDateTime.now().minusDays(1), ibans, ibans);
    }

    //check if the account is a savings account and if it is, check if the user reference id is the same
    private boolean CheckForSavingsAccount(Account fromAccount, Account toAccount){
        if (fromAccount.getAccountType() == AccountType.SAVINGS || toAccount.getAccountType() == AccountType.SAVINGS) {
            return fromAccount.getUserReferenceId() == toAccount.getUserReferenceId();
        }
        return true;
    }

    public List<TransactionResponceDTO> GetAllFromUser(long userId){
        List<String> ibans = accountRepository.findAllIbansByUserReferenceId(userId);
        return ConvertToResponce(transactionRepository.findAllByFromAccountInOrToAccountIn(ibans, ibans));
    }

    public boolean ValidateWithdraw(DepositWithdrawDTO dto){
        return HasBalance(dto.getIban(), dto.getAmount()) && accountRepository.findByIban(dto.getIban()).getAbsoluteLimit() <= accountRepository.findByIban(dto.getIban()).getBalance() - dto.getAmount() && dto.getAmount() > 0 && CheckDailyLimit(dto.getIban(), dto.getAmount(), userRepository.findUserDayLimitById(accountRepository.findByIban(dto.getIban()).getUserReferenceId()));
    }

    public Transaction withdraw(DepositWithdrawDTO dto){
        Account account = accountRepository.findByIban(dto.getIban());
        accountService.update(new patchAccountDTO(account.getIban(), "update", "balance", Double.toString(account.getBalance() - dto.getAmount())));
        return transactionRepository.save(new Transaction(LocalDateTime.now(), account.getIban(), "withdraw", dto.getAmount(), "Withdraw"));
    }

    public boolean ValidateDeposit(DepositWithdrawDTO dto){
        return dto.getAmount() > 0 && dto.getAmount() <= 1000;
    }

    public Transaction deposit(DepositWithdrawDTO dto){
        Account account = accountRepository.findByIban(dto.getIban());
        accountService.update(new patchAccountDTO(account.getIban(), "update", "balance", Double.toString(account.getBalance() + dto.getAmount())));
        return transactionRepository.save(new Transaction(LocalDateTime.now(), "deposit", account.getIban(), dto.getAmount(), "Deposit"));
    }

    private TransactionResponceDTO ConvertToResponce(Transaction transaction){
        if (transaction.getFromAccount() == "deposit")
            return new TransactionResponceDTO(transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount(), transaction.getDescription(), transaction.getTimestamp(), "+" );
        Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
        return new TransactionResponceDTO(transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount(), transaction.getDescription(), transaction.getTimestamp(), GetDirection(transaction.getFromAccount(), fromAccount.getUserReferenceId()));
    }

    private List<TransactionResponceDTO> ConvertToResponce(List<Transaction> transactions) {
        List<TransactionResponceDTO> responce = new ArrayList<>();
        for (Transaction transaction : transactions) {
            responce.add(ConvertToResponce(transaction));
        }
        return responce;
    }
}
