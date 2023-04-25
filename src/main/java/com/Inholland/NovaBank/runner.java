package com.Inholland.NovaBank;

import com.Inholland.NovaBank.model.Transaction;
import com.Inholland.NovaBank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class runner implements ApplicationRunner {

    @Autowired
    private TransactionService transactionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        transactionService.Add(new Transaction(LocalDateTime.now(),"NL78RABO9043081477", "NL90RABO3696794244", 100, "Test transaction"));
        transactionService.Add(new Transaction(LocalDateTime.now(),"NL78RABO9043081477", "NL90RABO3696794244", 100, "Test transaction"));
        transactionService.Add(new Transaction(LocalDateTime.now(),"NL78RABO9043081477", "NL90RABO3696794244", 10, "Test transaction"));
    }
}
