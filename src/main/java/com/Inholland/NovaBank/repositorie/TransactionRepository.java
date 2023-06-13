package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findAllByFromAccountOrToAccount(String fromAccount, String toAccount);

    List<Transaction> findAllByFromAccountOrToAccountAndTimestampAfter(String fromAccount, String toAccount, LocalDateTime date);

    List<Transaction> findAllByFromAccountInOrToAccountIn(List<String> fromAccount, List<String> toAccount);

    @Query("SELECT coalesce(SUM(t.amount), 0) FROM Transaction t WHERE t.fromAccount = ?1 AND t.toAccount NOT IN ?2 AND t.timestamp > ?3")
    Double findSumOfTransactionsFromAccount(String fromAccount, List<String> toAccount, LocalDateTime date);
}
