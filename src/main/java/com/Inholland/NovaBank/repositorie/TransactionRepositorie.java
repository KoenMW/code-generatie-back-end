package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepositorie extends CrudRepository<Transaction, Long> {
    public List<Transaction> findAllByFromAccountOrToAccount(String fromAccount, String toAccount);
}
