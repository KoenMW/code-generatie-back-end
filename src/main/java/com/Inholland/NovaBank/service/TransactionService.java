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
import java.util.Objects;

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

    public List<TransactionResponceDTO> GetAll(){
        return ConvertToResponse((List<Transaction>) transactionRepository.findAll(), 0);
    }

    public List<TransactionResponceDTO> GetAllFromIban(String Iban){
        List<Transaction> transactions = transactionRepository.findAllByFromAccountOrToAccount(Iban, Iban);
        return ConvertToResponse(transactions, accountRepository.findByIban(Iban).getUserReferenceId());
    }

    public TransactionResponceDTO Add(TransactionRequestDTO transaction) {
        if (transaction.getDescription().length() == 0)
            transaction.setDescription("No description");
        Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
        Account toAccount = accountRepository.findByIban(transaction.getToAccount());
        if (fromAccount != null && toAccount != null)
        {
            accountService.updateBalance(new patchAccountDTO(fromAccount.getIban(), "update", "balance", Double.toString(fromAccount.getBalance() - transaction.getAmount())));
            accountService.updateBalance(new patchAccountDTO(toAccount.getIban(), "update", "balance", Double.toString(toAccount.getBalance() + transaction.getAmount())));
        }
        Transaction saved = transactionRepository.save(new Transaction(LocalDateTime.now(), transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount(), transaction.getDescription(), transaction.getUserId()));
        return ConvertToResponse(saved, transaction.getUserId());
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

    //checks if the account exists and if the account has enough balance to make the transaction and if the absolute limit is not exceeded
    public boolean ValidateTransaction(TransactionRequestDTO transaction){
        if (accountService.AccountExists(transaction.getFromAccount()) && accountService.AccountExists(transaction.getToAccount())){
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
            if(!transaction.getDescription().equalsIgnoreCase("deposit")){
                totalAmount += transaction.getAmount();
            }
        }
        return totalAmount + amount <= dailyLimit;
    }


    //gets a list of transactions from the last 24 hours
    private List<Transaction> GetTransactionsFromLast24Hours(String iban){
        return transactionRepository.findAllByFromAccountAndTimestampAfter(iban, LocalDateTime.now().minusDays(1));
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
        return ConvertToResponse(transactionRepository.findAllByFromAccountInOrToAccountIn(ibans, ibans), userId);
    }

    public boolean ValidateWithdraw(DepositWithdrawDTO dto){
        return HasBalance(dto.getIban(), dto.getAmount()) && accountRepository.findByIban(dto.getIban()).getAbsoluteLimit() <= accountRepository.findByIban(dto.getIban()).getBalance() - dto.getAmount() && dto.getAmount() > 0 && CheckDailyLimit(dto.getIban(), dto.getAmount(), userRepository.findUserDayLimitById(accountRepository.findByIban(dto.getIban()).getUserReferenceId()));
    }

    public TransactionResponceDTO Withdraw(DepositWithdrawDTO dto){
        Account account = accountRepository.findByIban(dto.getIban());
        accountService.updateBalance(new patchAccountDTO(account.getIban(), "update", "balance", Double.toString(account.getBalance() - dto.getAmount())));
        return ConvertToResponse(transactionRepository.save(new Transaction(LocalDateTime.now(), account.getIban(), "withdraw", dto.getAmount(), "Withdraw", dto.getUserId())), dto.getUserId());
    }

    public boolean ValidateDeposit(DepositWithdrawDTO dto){
        return dto.getAmount() > 0 && dto.getAmount() <= 1000;
    }

    public TransactionResponceDTO Deposit(DepositWithdrawDTO dto){
        Account account = accountRepository.findByIban(dto.getIban());
        accountService.updateBalance(new patchAccountDTO(account.getIban(), "update", "balance", Double.toString(account.getBalance() + dto.getAmount())));
        return ConvertToResponse(transactionRepository.save(new Transaction(LocalDateTime.now(), "deposit", account.getIban(), dto.getAmount(), "Deposit", dto.getUserId())), dto.getUserId());
    }

    private TransactionResponceDTO ConvertToResponse(Transaction transaction, long userId){
        List<String> ibans = accountRepository.findAllIbansByUserReferenceId(userId);
        if (Objects.equals(transaction.getFromAccount(), "deposit") || ibans.contains(transaction.getToAccount()) )
            return new TransactionResponceDTO(transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount(), transaction.getDescription(), transaction.getTimestamp(), "+" );
        Account fromAccount = accountRepository.findByIban(transaction.getFromAccount());
        return new TransactionResponceDTO(transaction.getFromAccount(), transaction.getToAccount(), transaction.getAmount(), transaction.getDescription(), transaction.getTimestamp(), "-" );
    }

    private List<TransactionResponceDTO> ConvertToResponse(List<Transaction> transactions, long userId){
        List<TransactionResponceDTO> response = new ArrayList<>();
        for (Transaction transaction : transactions) {
            response.add(ConvertToResponse(transaction, userId));
        }
        return response;
    }
}
