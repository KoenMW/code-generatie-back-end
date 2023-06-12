package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.service.BaseService;
import org.iban4j.Iban;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BaseServiceTest {

    @Test
    void isValidIban() {
        String iban = BaseService.generateIban();
        assertTrue(BaseService.IsValidIban(iban));
    }

    @Test
    void isValidIbanFalse() {
        String iban = "NL00INHO0000000000";
        assertFalse(BaseService.IsValidIban(iban));
    }

    @Test
    void generateIban() {
        String iban = BaseService.generateIban();
        assertNotNull(iban);
    }

    @Test
    void generateIbanWithCountryCode() {
        String iban = BaseService.generateIban();
        assertNotNull(iban);
        assertEquals("NL", Iban.valueOf(iban).getCountryCode().toString());
    }


}