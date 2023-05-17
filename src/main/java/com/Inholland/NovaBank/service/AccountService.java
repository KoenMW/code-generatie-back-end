package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class AccountService extends BaseService{
    @Autowired
    private AccountRepository accountRepository;
    private Account accountFromRepo;

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
        return new OffsetBasedPageRequest(offset.intValue(), limit.intValue());
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

    public returnAccountDTO update(patchAccountDTO account){
        Account accountFromRepo = accountRepository.findByIban(account.getIban());
        switch (account.getKey()) {
            case "iban" -> accountFromRepo.setIban(account.getValue());
            case "active" -> accountFromRepo.setActive(Boolean.parseBoolean(account.getValue()));
            case "accountType" -> accountFromRepo.setAccountType(AccountType.valueOf(account.getValue()));
            case "absoluteLimit" -> accountFromRepo.setAbsoluteLimit((float) Double.parseDouble(account.getValue()));
            case "balance" -> accountFromRepo.setBalance((float) Double.parseDouble(account.getValue()));
            case "userReferenceId" -> accountFromRepo.setUserReferenceId(Long.parseLong(account.getValue()));
            default -> {
                return null;
            }
        }


        Account account1 = accountRepository.save(accountFromRepo);
        returnAccountDTO returnAccountDTO;
        return returnAccountDTO = new returnAccountDTO(account1.getIban(), account1.getAccountType());
    }

    public void delete(long id){
        accountRepository.deleteById(id);
    }

    public void deleteByAccount(Account account){
        accountRepository.delete(account);
    }



}
