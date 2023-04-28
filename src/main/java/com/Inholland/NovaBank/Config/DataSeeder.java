package com.Inholland.NovaBank.Config;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.service.AccountService;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.add(new User("John", "Doe",null));
        List<User> users = userService.getAll();

        accountService.add(new Account("NL29INGB123123", 1000, users.get(0).getId(), users.get(0), AccountType.CHECKING,"EUR", true,100));
        accountService.add(new Account("NL29INGB123123", 13000, users.get(0).getId(),users.get(0),  AccountType.SAVINGS,"EUR", true,1000));
        List<Account> accounts = accountService.getAll();

        Account account = accountService.getById(accounts.get(0).getId());
        users.get(0).setBankAccounts(List.of(account));
        userService.update(users.get(0));
        List<User> users2 = userService.getAll();
        //System.out.println(users2.get(0).getBankAccounts().get(0).getBalance());
        System.out.println("Done seeding data");


    }


}
