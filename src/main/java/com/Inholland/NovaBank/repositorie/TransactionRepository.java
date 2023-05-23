package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public List<Transaction> findAllByFromAccountOrToAccount(String fromAccount, String toAccount);

    public List<Transaction> findAllByFromAccountOrToAccountAndTimestampAfter(String fromAccount, String toAccount, LocalDateTime date);

    public List<Transaction> findAllByFromAccountInOrToAccountIn(List<String> fromAccount, List<String> toAccount);
}
