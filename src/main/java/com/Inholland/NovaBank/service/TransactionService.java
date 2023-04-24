package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.repositorie.TransactionRepositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService extends BaseService {

    @Autowired
    private TransactionRepositorie transactionRepositorie;

    public List<Transaction> GetAll(){
        return (List<Transaction>) transactionRepositorie.findAll();
    }

    public List<Transaction> GetAllFromIban(String Iban){
        return transactionRepositorie.findAllByFromAccount(Iban);
    }

    public void Add(Transaction transaction) {
        if (transaction.getDescription().length() == 0)
            transaction.setDescription("No description");
        transactionRepositorie.save(transaction);
    }
}
