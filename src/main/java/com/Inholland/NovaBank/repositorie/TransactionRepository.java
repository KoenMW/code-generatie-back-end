package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public List<Transaction> findAllByFromAccount(String fromAccount);
}
