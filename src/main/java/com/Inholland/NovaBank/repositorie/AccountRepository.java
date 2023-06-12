package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Account;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    //Voor pagination request
    @Query("SELECT a FROM Account a ORDER BY a.iban")
    List<Account> findAllAccounts(Pageable pageable);
    //Voor pagination request maar dan met actieve accounts
    @Query("SELECT a FROM Account a WHERE a.active = ?1 ORDER BY a.iban")
    List<Account> findAllAccountsActive(Pageable pageable, boolean active);

    //Voor het ophalen van alle accounts van een gebruiker
    List<Account> findByuserReferenceId(long id);
    //Voor het ophalen van een account met een iban
    Account findByIban(String iban);

    //Voor het ophalen van alle ibans van een gebruiker
    @Query("SELECT a.iban FROM Account a WHERE a.userReferenceId = ?1")
    List<String> findAllIbansByUserReferenceId(long id);
}
