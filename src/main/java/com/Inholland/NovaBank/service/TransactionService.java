package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.repositorie.TransactionRepositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepositorie transactionRepositorie;
    

}
