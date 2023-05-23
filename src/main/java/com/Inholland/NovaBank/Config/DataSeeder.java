package com.Inholland.NovaBank.Config;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newUserDTO;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.Role;
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


        userService.addUser(new newUserDTO("John", "Doe", "JohnDoe", "123h4jg893n", "John@doe.nl"));
        userService.addUser(new newUserDTO("Bank", "Bank", "Bank", "123h4jg893n", "novaBank@bank.nl"));
        userService.addUser(new newUserDTO("Henk","Blok","henk","1234","henk@gmail.com"));

        List<User> users = userService.getAll();

        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.CHECKING,100));
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        accountService.add(new newAccountDTO(users.get(1).getId(), AccountType.SAVINGS,100));



        List<Account> accounts = accountService.getAll(1000L,0L);
        String id = accounts.get(0).getIban();
        patchAccountDTO patchAccountDTO = new patchAccountDTO();
        patchAccountDTO.setOp("update");
        patchAccountDTO.setKey("iban");
        patchAccountDTO.setValue("NL18INHO0363662776");
        patchAccountDTO.setIban(id);
        accountService.update(patchAccountDTO);
        User user = userService.getById(1L);
        user.setRole(Role.ROLE_ADMIN);
        userService.update(user);
        for(Account account : accounts){
            if(account.getUserReferenceId() == 2){
                seedBaseAccount(account.getIban());
            }
        }


        System.out.println("Done seeding data");


        for (Account account:
             accounts) {
            System.out.println(account.getIban());
        }
        transactionService.Add(new Transaction(LocalDateTime.now(), accounts.get(0).getIban(), accounts.get(1).getIban(), 100, "Test transaction"));
        transactionService.Add(new Transaction(LocalDateTime.now(), accounts.get(0).getIban(), accounts.get(1).getIban(), 100, "Test transaction"));
        transactionService.Add(new Transaction(LocalDateTime.now(), accounts.get(0).getIban(), accounts.get(1).getIban(), 10, "Test transaction"));
    }

    private void seedBaseAccount(String id){
        patchAccountDTO patchAccountDTO = new patchAccountDTO();
        patchAccountDTO.setOp("update");
        patchAccountDTO.setKey("iban");
        patchAccountDTO.setValue("NL01INHO0000000001");
        patchAccountDTO.setIban(id);
        accountService.update(patchAccountDTO);

    }


}
