package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepositorie extends CrudRepository<Transaction, Long> {
    public List<Transaction> findAllByFromAccountOrToAccount(String fromAccount, String toAccount);

    //find all transactions by fromAccount or toAccount where the localDate is from the last 24 hours:
    public List<Transaction> findAllByFromAccountOrToAccountAndTimestampAfter(String fromAccount, String toAccount, LocalDateTime date);
}
