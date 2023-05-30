package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public List<Transaction> findAllByFromAccountOrToAccount(String fromAccount, String toAccount);

    public List<Transaction> findAllByFromAccountOrToAccountAndTimestampAfter(String fromAccount, String toAccount, LocalDateTime date);

    public List<Transaction> findAllByFromAccountInOrToAccountIn(List<String> fromAccount, List<String> toAccount);

    //find all transactions from a specific account excluding the ones that are older than a specific date and excluding the ones that are from a list of given accounts:
    public List<Transaction> findAllByFromAccountAndTimestampAfterAndFromAccountNotInOrToAccountNotIn(String fromAccount, LocalDateTime date, List<String> fromAccountList, List<String> toAccountList);

    //find the sum of all transactions from a specific account excluding the ones that where the to account is in a list of given accounts and excluding the ones older than the last 24 hours:
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.fromAccount = ?1 AND t.timestamp > ?2 AND t.toAccount NOT IN ?3 GROUP BY t.fromAccount")
    public double findSumOfAllTransactionsFromAccount(String fromAccount, LocalDateTime date, List<String> toAccountList);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.fromAccount = ?1 AND t.timestamp > ?2 AND t.fromAccount NOT IN ?3 AND t.toAccount NOT IN ?4 GROUP BY t.fromAccount")
    //@Query("SELECT SUM FROM Transaction t WHERE t.fromAccount = ?1 AND t.timestamp > ?2 AND t.fromAccount NOT IN ?3 AND t.toAccount NOT IN ?4")
    public double findSumOfAllTransactionsFromAccount(String fromAccount, LocalDateTime date, List<String> fromAccountList, List<String> toAccountList);

}
