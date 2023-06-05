package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import com.Inholland.NovaBank.repositorie.UserRepository;
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

    public List<Account> getAll(boolean isActive, Long limit, Long offset){

        if (limit == null) {
            limit = 1000L;
        }
        if (offset == null) {
            offset = 0L;
        }

        if (isActive) {
            return getAllActive(limit, offset, true);
        } else {
            return getAll(limit, offset);
        }


    }

    public List<Account> getAllActive(Long limit, Long offset, boolean active){
        return accountRepository.findAllAccountsActive(getPageable(limit, offset), active);
    }

    public List<Account> getAll(Long limit, Long offset){
        return accountRepository.findAllAccounts(getPageable(limit, offset));
    }

    public Pageable getPageable(Long limit, Long offset) {
        return new OffsetBasedPageRequest(offset.intValue(), limit.intValue());
    }

    public List<Account> getByUserId(long id){
        if(authUser(id)){
            return accountRepository.findByuserReferenceId(id);
        }
        else{
            throw new IllegalArgumentException("Not authorized");
        }

    }

    public boolean authUser(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentPrincipalName = authentication.getName();
        returnUserDTO user = userService.getUserByUsername(currentPrincipalName);
        if(user.getRole().toString().equals("ROLE_ADMIN")){
            return true;
        } else return user.getId() == id;

    }

    public returnAccountDTO add(newAccountDTO account){
        if(!checkUserHasAccount(account.getUserReferenceId())){
            updateUserAccountStatus(account.getUserReferenceId());
        }
        if(!checkLimit(account.getAbsoluteLimit())){
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        Account newAccount = setAccount(account);
        Account accountFromRepo = accountRepository.save(newAccount);
        return new returnAccountDTO(accountFromRepo.getIban(), accountFromRepo.getAccountType());

    }
    public Boolean checkLimit(float limit){
        return limit >= 0 && limit < 1000000;
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
        returnUserDTO user = userService.getByIdDataSeeder(id);
        return user.isHasAccount();
    }

    private void updateUserAccountStatus(long id){
        returnUserDTO user = userService.getByIdDataSeeder(id);
        patchUserDTO patchUserDTO = new patchUserDTO();
        patchUserDTO.setKey("hasAccount");
        patchUserDTO.setValue("true");
        patchUserDTO.setId(id);
        patchUserDTO.setOp("update");
        userService.update(patchUserDTO);
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

    public AccountService(AccountRepository accountRepository, UserRepository userRepository,UserService userService){
        this.accountRepository = accountRepository;
        this.userService = userService;
    }






}
