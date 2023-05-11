package com.Inholland.NovaBank.Config;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.model.User;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.add(new User("John", "Doe"));
        List<User> users = userService.getAll();

        accountService.add(new Account("NL29INGB123123", 1000.0, users.get(0).getId(),  AccountType.CHECKING,"EUR", true,100));
        accountService.add(new Account("NL78RABO9043081477", 13000.0, users.get(0).getId(),  AccountType.SAVINGS,"EUR", true,1000));
        List<Account> accounts = accountService.getAll();

        Account account = accountService.getById(accounts.get(0).getId());

        userService.update(users.get(0));
        List<User> users2 = userService.getAll();
        //System.out.println(users2.get(0).getBankAccounts().get(0).getBalance());
        System.out.println("Done seeding data");




        //transactionService.Add(new Transaction(LocalDateTime.now(),"NL78RABO9043081477", "NL29INGB123123", 100, "Test transaction"));
        //transactionService.Add(new Transaction(LocalDateTime.now(),"NL29INGB123123", "NL78RABO9043081477", 100, "Test transaction"));
        //transactionService.Add(new Transaction(LocalDateTime.now(),"NL78RABO9043081477", "NL29INGB123123", 10, "Test transaction"));
    }


}
