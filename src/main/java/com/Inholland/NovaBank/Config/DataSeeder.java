package com.Inholland.NovaBank.Config;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.TransactionRepository;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import com.Inholland.NovaBank.service.AccountService;
import com.Inholland.NovaBank.service.TransactionService;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.Inholland.NovaBank.service.BaseService.generateIban;

@Component
public class DataSeeder implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        userService.addUser(new newUserDTO("John", "Doe", "JohnDoe", "1234567", "John@doe.nl"));
        userService.addUser(new newUserDTO("Bank", "Bank", "Bank", "123h4jg893n", "novaBank@bank.nl"));
        userService.addUser(new newUserDTO("Henk","Blok","henk","1234567","henk@gmail.com"));

        List<returnUserDTO> users = userService.getAll(1000L, 0L);

        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        accountService.add(new newAccountDTO(users.get(1).getId(), AccountType.SAVINGS,100));

        List<Account> accounts = accountService.getAll(1000L,0L);

        accountRepository.save(new Account("NL18INHO0363662776", 300, 2, AccountType.CHECKING, true,100));
        accountRepository.save(new Account("NL12INHO0154160635", 300, 1, AccountType.CHECKING, true,100));

        transactionRepository.save(new Transaction( LocalDateTime.now().minusDays(1), "NL18INHO0363662776", "NL12INHO0154160635", 100, "test"));
        transactionRepository.save(new Transaction( LocalDateTime.now().minusDays(1), "NL12INHO0154160635", "NL18INHO0363662776", 100, "test"));


        returnUserDTO user = userService.getByIdDataSeeder(1L);
        patchUserDTO patchUserDTO = new patchUserDTO();
        patchUserDTO.setKey("role");
        patchUserDTO.setValue("ROLE_ADMIN");
        patchUserDTO.setId(user.getId());
        patchUserDTO.setOp("update");
        userService.update(patchUserDTO);
        for(Account account : accounts){
            if(account.getUserReferenceId() == 2){
                seedBaseAccount(account.getUserReferenceId());
            }
        }





        transactionService.Deposit(new DepositWithdrawDTO(accounts.get(0).getIban(), 200));
        transactionService.Deposit(new DepositWithdrawDTO(accounts.get(1).getIban(), 50));
        transactionService.Deposit(new DepositWithdrawDTO(accounts.get(2).getIban(), 150));

        transactionRepository.save(new Transaction(LocalDateTime.now().minusDays(1), accounts.get(0).getIban(), accounts.get(1).getIban(), 50, "Boodschappen"));
        transactionRepository.save(new Transaction(LocalDateTime.now().minusDays(1), accounts.get(0).getIban(), accounts.get(2).getIban(), 50, "Starbucks"));

        transactionRepository.save(new Transaction(LocalDateTime.now().minusDays(2), accounts.get(0).getIban(), accounts.get(1).getIban(), 50, "Oma"));
        transactionRepository.save(new Transaction(LocalDateTime.now().minusDays(2), accounts.get(0).getIban(), accounts.get(2).getIban(), 50, "Bol.com"));
        System.out.println("Done seeding data");
    }

    private void seedBaseAccount(Long id){

        accountRepository.save(new Account("NL01INHO0000000001", 0, id, AccountType.SAVINGS, true,100));
    }
}
