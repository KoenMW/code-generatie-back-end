package com.Inholland.NovaBank.service;


import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.*;

import com.Inholland.NovaBank.repositorie.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AccountService extends BaseService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    //Methode om alles op te halen controleert of de account actief is en of er een limiet is meegegeven en een offset
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

    //Methode om alle actieve accounts op te halen
    public List<Account> getAllActive(Long limit, Long offset, boolean active){
        return accountRepository.findAllAccountsActive(getPageable(limit, offset), active);
    }

    //Methode om alle accounts op te halen
    public List<Account> getAll(Long limit, Long offset){
        return accountRepository.findAllAccounts(getPageable(limit, offset));
    }

    //Methode om accounts op te halen voor anonieme gebruikers bij de zoekfunctie met minder informatie
    public List<searchAccountDTO> getAllSearch(Long limit, Long offset){
        if(limit == null){
            limit = 1000L;
        }
        if(offset == null){
            offset = 0L;
        }
        return transformAccounts(accountRepository.findAllAccounts(getPageable(limit, offset)));
    }

    //Methode om accounts om te zetten in een DTO voor anonieme gebruikers
    public List<searchAccountDTO> transformAccounts(List<Account> accounts){
        List<searchAccountDTO> searchAccountDTOS = new ArrayList<>();
        for (Account account : accounts) {
            searchAccountDTOS.add(new searchAccountDTO(account.getIban(), account.getUserReferenceId(),account.getAccountType()));
        }
        return searchAccountDTOS;
    }

    //Maakt een pageable object aan voor de offset en limit
    public Pageable getPageable(Long limit, Long offset) {
        return new OffsetBasedPageRequest(offset.intValue(), limit.intValue());
    }

    //Methode om een account op te halen op basis van de id
    public List<Account> getByUserId(long id){
        if(authUser(id)){
            return accountRepository.findByuserReferenceId(id);
        }
        else{
            throw new IllegalArgumentException("Not authorized");
        }
    }

    //Alleen admin mag het ophalen of de gebruiker zelf
    public boolean authUser(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        returnUserDTO user = userService.getUserByUsername(currentPrincipalName);
        if(user.getRole().toString().equals("ROLE_ADMIN")){
            return true;
        } else return user.getId() == id;
    }

    //Methode om een nieuw account aan te maken
    //Controleert of de gebruiker al een account heeft en of de limiet juist is
    public returnAccountDTO add(newAccountDTO account){
        if(!checkUserHasAccount(account.getUserReferenceId())){
            updateUserAccountStatus(account.getUserReferenceId());
        }
        if(!checkLimit(account.getAbsoluteLimit())){
            throw new IllegalArgumentException("Limit must be greater than or equal to 0 and less than 1000000");
        }
        Account newAccount = setAccount(account);
        Account accountFromRepo = accountRepository.save(newAccount);
        return new returnAccountDTO(accountFromRepo.getIban(), accountFromRepo.getAccountType());

    }

    //Simpele methode om limiet te controleren
    public Boolean checkLimit(float limit){
        return limit >= 0 && limit < 1000000;
    }

    //Methode om een account in te vullen
    public Account setAccount(newAccountDTO account){
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setBalance(0);
        newAccount.setActive(true);
        newAccount.setAccountType(account.getAccountType());
        newAccount.setAbsoluteLimit(account.getAbsoluteLimit());
        newAccount.setUserReferenceId(account.getUserReferenceId());
        return newAccount;
    }

    //Methode om te kijken of de gebruiker al een account heeft
    public boolean checkUserHasAccount(long id){
        returnUserDTO user = userService.getByIdDataSeeder(id);
        return user.isHasAccount();
    }

    //Zorgt ervoor dat de useraccountstatus wordt geupdate
    public void updateUserAccountStatus(long id){
        patchUserDTO patchUserDTO = new patchUserDTO();
        patchUserDTO.setKey("hasAccount");
        patchUserDTO.setValue("true");
        patchUserDTO.setId(id);
        patchUserDTO.setOp("update");
        userService.update(patchUserDTO);
    }

    //Methode om accounts aan te passen
    //Controleert of de gebruiker niet de bank wil aanpassen
    //Het is een patch dus gaat 1 veld aanpassen
    public returnAccountDTO update(patchAccountDTO account)  {
        Account accountFromRepo = accountRepository.findByIban(account.getIban());
        if(ownership(accountFromRepo)){
            throw new IllegalArgumentException("Not authorized");
        }
        switch (account.getKey()) {
            case "iban" -> accountFromRepo.setIban(account.getValue());
            case "active" -> accountFromRepo.setActive(Boolean.parseBoolean(account.getValue()));
            case "accountType" -> accountFromRepo.setAccountType(AccountType.valueOf(account.getValue()));
            case "absoluteLimit" -> {
                if(checkLimit(Float.parseFloat(account.getValue()))){
                    accountFromRepo.setAbsoluteLimit((float) Double.parseDouble(account.getValue()));
                }
                else{
                    throw new IllegalArgumentException("Invalid limit, must be greater or equal to 0 and less than 1000000");
                }
            }
            case "balance" -> accountFromRepo.setBalance((float) Double.parseDouble(account.getValue()));
            case "userReferenceId" -> accountFromRepo.setUserReferenceId(Long.parseLong(account.getValue()));
            default -> {
                return null;
            }
        }
        Account account1 = accountRepository.save(accountFromRepo);
        return new returnAccountDTO(account1.getIban(), account1.getAccountType());
    }

    //Controleert of het niet de bank is
    private boolean ownership(Account accountFromRepo) {
        return accountFromRepo.getIban().equals("NL01INHO0000000001");
    }

    //Methode voor de transaction om balans te updaten
    public returnAccountDTO updateBalance(patchAccountDTO account){
        Account accountFromRepo = accountRepository.findByIban(account.getIban());
        accountFromRepo.setBalance(Float.parseFloat(account.getValue()));
        Account account1 = accountRepository.save(accountFromRepo);
        return new returnAccountDTO(account1.getIban(), account1.getAccountType());
    }

    //Methode om te controleren of account bestaat
    public boolean AccountExists(String Iban){
        return accountRepository.findByIban(Iban) != null;
    }






}
