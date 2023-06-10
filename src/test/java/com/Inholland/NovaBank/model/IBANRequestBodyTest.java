package com.Inholland.NovaBank.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class IBANRequestBodyTest {
    @Test
    void testConstructor() {
        IBANRequestBody actualIBANRequestBody = new IBANRequestBody("NL01INHO0000000001");
        assertNotNull(actualIBANRequestBody);
    }
    @Test
    void testConstructor2() {
        IBANRequestBody actualIBANRequestBody = new IBANRequestBody();
        assertNull(actualIBANRequestBody.getIban());
    }

    @Test
    void testGetIban() {
        IBANRequestBody ibanRequestBody = new IBANRequestBody();
        assertNull(ibanRequestBody.getIban());
    }

    @Test
    void getIban(){
        IBANRequestBody ibanRequestBody = new IBANRequestBody("NL01INHO0000000001");
        assertEquals("NL01INHO0000000001", ibanRequestBody.getIban());
    }

}
