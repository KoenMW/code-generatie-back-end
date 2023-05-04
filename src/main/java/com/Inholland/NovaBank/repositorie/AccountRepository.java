package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByuserReferenceIdAndStatus(long id, boolean status);
}
