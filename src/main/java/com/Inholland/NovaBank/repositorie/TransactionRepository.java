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

    //find all transactions from a specific account excluding the ones that are older than a specific date and excluding the ones that are from a list of given accounts:
    public List<Transaction> findAllByFromAccountAndTimestampAfterAndFromAccountNotInOrToAccountNotIn(String fromAccount, LocalDateTime date, List<String> fromAccountList, List<String> toAccountList);
}
