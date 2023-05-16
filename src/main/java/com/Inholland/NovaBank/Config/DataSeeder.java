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
        userService.add(new User(null, "John", "Doe", "JohnDoe", "123h4jg893n", "John@doe.nl", "employee", 3000, 1500, true));
        List<User> users = userService.getAll();

        accountService.add(new Account("NL29INGB123123", 1000, users.get(0), AccountType.CHECKING,"EUR", true,100));
        List<Account> accounts = accountService.getAll();

        Account account = accountService.getById(accounts.get(0).getId());
        users.get(0).setBankAccounts(List.of(account));
        userService.update(users.get(0));
        List<User> users2 = userService.getAll();
        try{
            System.out.println(users2.get(0).getBankAccounts().get(0).getId());
            for (Account a: accounts) {
                System.out.println(a.getId());
                System.out.println(a.getBalance());
                System.out.println(a.getIban());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }


}
