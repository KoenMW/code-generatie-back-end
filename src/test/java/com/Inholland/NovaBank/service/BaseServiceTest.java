package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.service.BaseService;
import org.iban4j.Iban;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseServiceTest {

    @Test
    void isValidIban() {
        String iban = BaseService.generateIban();
        assertTrue(BaseService.IsValidIban(iban));
    }

    @Test
    void generateIban() {
        String iban = BaseService.generateIban();
        assertNotNull(iban);
    }
}