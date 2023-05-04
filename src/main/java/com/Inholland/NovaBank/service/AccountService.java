package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
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
        return accountRepository.findByuserReferenceIdAndStatus(id, true);
    }

    public Account add(Account account){
        account.setIban(generateIban());
        account.setBalance(0);
        account.setStatus(true);
        account.setCurrency("EUR");

        return accountRepository.save(account);
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
