package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService extends BaseService{
    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<List<Account>> getAll(boolean isActive, Long limit, Long offset){
        if(isActive){
            if(limit == null){
                limit = 1000L;
            }
            if(offset == null){
                offset = 0L;
            }

            return ResponseEntity.status(200).body(getAllActive(limit, offset,isActive));
        }
        else{
            if(limit == null){
                limit = 1000L;
            }
            if(offset == null){
                offset = 0L;
            }
            return ResponseEntity.status(200).body(getAll(limit, offset));
        }


    }

    public List<Account> getAllActive(Long limit, Long offset, boolean active){
        return accountRepository.findAllAccountsActive(getPageable(limit, offset), active);
    }

    public List<Account> getAll(Long limit, Long offset){
        return accountRepository.findAllAccounts(getPageable(limit, offset));
    }

    private Pageable getPageable(Long limit, Long offset) {
        return PageRequest.of(offset.intValue(), limit.intValue());
    }

    public List<Account> getAllActive(){
        return accountRepository.findByActive(true);
    }

    public Account getByIban(String iban){
        return accountRepository.findByIban(iban);
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
        newAccount.setUserReferenceId(account.getUserReferenceId());

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
