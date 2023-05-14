package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("SELECT a FROM Account a ORDER BY a.iban")
    List<Account> findAllAccounts(Pageable pageable);

    @Query("SELECT a FROM Account a WHERE a.active = ?1 ORDER BY a.iban")
    List<Account> findAllAccountsActive(Pageable pageable, boolean active);


    List<Account> findByuserReferenceId(long id);
    Account findByIban(String iban);

    List<Account> findByActive(boolean active);


}
