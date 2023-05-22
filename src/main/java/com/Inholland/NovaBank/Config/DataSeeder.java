package com.Inholland.NovaBank.Config;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.service.AccountService;
import com.Inholland.NovaBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.Inholland.NovaBank.service.BaseService.generateIban;

@Component
public class DataSeeder implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        User user = new User("John", "Doe", "JohnDoe", "123h4jg893n", "John@doe.nl", Role.ROLE_ADMIN, 3000, 1500, true);
        User user2 = new User("Bank", "Bank", "Bank", "123h4jg893n", "novaBank@bank.nl", Role.ROLE_ADMIN, 3000, 1500, true);
        User user3 = new User("Henk","Blok","henk","1234","henk@gmail.com", Role.ROLE_USER, 3000, 1500, true);

        userService.addUser(user);
        userService.addUser(user2);
        userService.addUser(user3);

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
        for(Account account : accounts){
            if(account.getUserReferenceId() == 2){
                seedBaseAccount(account.getIban());
            }
        }


        System.out.println("Done seeding data");


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
