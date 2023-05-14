package com.Inholland.NovaBank.Config;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
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
        userService.add(new User("John", "Doe"));
        List<User> users = userService.getAll();
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.CHECKING,100));
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        List<Account> accounts = accountService.getAll(1000L,0L);
        String id = accounts.get(0).getIban();
        patchAccountDTO patchAccountDTO = new patchAccountDTO();
        patchAccountDTO.setOp("update");
        patchAccountDTO.setKey("iban");
        patchAccountDTO.setValue("NL18INHO0363662776");
        patchAccountDTO.setIban(id);
        accountService.update(patchAccountDTO);
        System.out.println(users.get(0).getId());




        System.out.println("Done seeding data");


    }


}
