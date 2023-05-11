package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByuserReferenceIdAndStatus(long id, boolean status);

    @Modifying
    @Query("update Account a set a.balance = :balance where a.id = :id")
    void setBalance(@Param("id") String id, @Param("balance") double balance);



    Account findByIban(String iban);
}
