package com.Inholland.NovaBank.Config;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
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
    private AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        userService.addUser(new newUserDTO("John", "Doe", "JohnDoe", "1234", "John@doe.nl"));
        userService.addUser(new newUserDTO("Bank", "Bank", "Bank", "123h4jg893n", "novaBank@bank.nl"));
        userService.addUser(new newUserDTO("Henk","Blok","henk","1234","henk@gmail.com"));

        List<returnUserDTO> users = userService.getAll(1000L, 0L);

        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.CHECKING,100));
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        accountService.add(new newAccountDTO(users.get(0).getId(), AccountType.SAVINGS,100));
        accountService.add(new newAccountDTO(users.get(1).getId(), AccountType.SAVINGS,100));

        List<Account> accounts = accountService.getAll(1000L,0L);

        accountRepository.save(new Account("NL18INHO0363662776", 0, 2, AccountType.SAVINGS, true,100));

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

        System.out.println("Done seeding data");


        for (Account account:
             accounts) {
            System.out.println(account.getIban());
        }
        transactionService.deposit(new DepositWithdrawDTO(accounts.get(0).getIban(), 200));
        transactionService.deposit(new DepositWithdrawDTO(accounts.get(1).getIban(), 50));
        transactionService.deposit(new DepositWithdrawDTO(accounts.get(2).getIban(), 150));
    }

    private void seedBaseAccount(Long id){

        accountRepository.save(new Account("NL01INHO0000000001", 0, id, AccountType.SAVINGS, true,100));
    }
}
