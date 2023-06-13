package com.Inholland.NovaBank.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class transactionTest {
    @Test
    void testConstructor() {
        Transaction actualTransaction = new Transaction(LocalDateTime.of(1, 1, 1, 1, 1), "From Account", "To Account", 10.0F, "Description");
        assertNotNull(actualTransaction);
    }

    @Test
    void testConstructor2(){
        Transaction actualTransaction = new Transaction();
        assertNotNull(actualTransaction);
    }


}
