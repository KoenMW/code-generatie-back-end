package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService extends BaseService{
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAll(){
        return (List<Account>) accountRepository.findAll();

    }

    public Account getById(long id){
        return accountRepository.findById(id).orElse(null);
    }

    public List<Account> getByUserId(long id){
        return accountRepository.findByuserReferenceId(id);
    }

    public returnAccountDTO add(newAccountDTO account){
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setBalance(0);
        newAccount.setActive(true);
        newAccount.setAccountType(account.getAccountType());
        newAccount.setAbsoluteLimit(account.getAbsoluteLimit());

        Account accountFromRepo = accountRepository.save(newAccount);
        return new returnAccountDTO(accountFromRepo.getIban(), accountFromRepo.getAccountType());

    }

    public Account update(Account account){
        return accountRepository.save(account);
    }

    public void delete(long id){
        accountRepository.deleteById(id);
    }

    public void deleteByAccount(Account account){
        accountRepository.delete(account);
    }



}
