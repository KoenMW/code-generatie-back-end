package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService extends BaseService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

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

    public List<Account> getByUserId(long id){
        if(authUser(id)){
            return accountRepository.findByuserReferenceId(id);
        }
        else{
            return null;
        }

    }

    private boolean authUser(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.getUserByUsername(currentPrincipalName);
        if(user.getRole().toString().equals("ROLE_ADMIN")){
            return true;
        } else return user.getId() == id;

    }

    public returnAccountDTO add(newAccountDTO account){
        if(!checkUserHasAccount(account.getUserReferenceId())){
            updateUserAccountStatus(account.getUserReferenceId());
        }
        Account newAccount = setAccount(account);
        Account accountFromRepo = accountRepository.save(newAccount);
        return new returnAccountDTO(accountFromRepo.getIban(), accountFromRepo.getAccountType());

    }
    private Account setAccount(newAccountDTO account){
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setBalance(0);
        newAccount.setActive(true);
        newAccount.setAccountType(account.getAccountType());
        newAccount.setAbsoluteLimit(account.getAbsoluteLimit());
        newAccount.setUserReferenceId(account.getUserReferenceId());
        return newAccount;
    }

    private boolean checkUserHasAccount(long id){
        User user = userService.getById(id);
        return user.isHasAccount();
    }

    private void updateUserAccountStatus(long id){
        User user = userService.getById(id);
        user.setHasAccount(true);
        userService.update(user);
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
        return new returnAccountDTO(account1.getIban(), account1.getAccountType());
    }





}
