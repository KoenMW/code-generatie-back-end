package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class newAccountDTest {
    @Test
    public void testConstructor() {
        new newAccountDTO(123L, AccountType.CHECKING, 10.0F);
        new newAccountDTO();
    }

    @Test
    public void testSetUserReferenceId() {
        newAccountDTO newAccountDTO = new newAccountDTO();
        newAccountDTO.setUserReferenceId(123L);
    }

    @Test
    public void testSetAccountType() {
        newAccountDTO newAccountDTO = new newAccountDTO();
        newAccountDTO.setAccountType(AccountType.CHECKING);
    }

    @Test
    public void testSetAbsoluteLimit() {
        newAccountDTO newAccountDTO = new newAccountDTO();
        newAccountDTO.setAbsoluteLimit(10.0F);
    }

    @Test
    public void testGetUserReferenceId() {
        newAccountDTO newAccountDTO = new newAccountDTO();
        newAccountDTO.setUserReferenceId(123L);
        assert newAccountDTO.getUserReferenceId() == 123L;
    }

    @Test
    public void testGetAccountType() {
        newAccountDTO newAccountDTO = new newAccountDTO();
        newAccountDTO.setAccountType(AccountType.CHECKING);
        assert newAccountDTO.getAccountType().equals(AccountType.CHECKING);
    }

    @Test
    public void emptyConstructor() {
        newAccountDTO newAccountDTO = new newAccountDTO();
        assertNull(newAccountDTO.getAccountType());
    }
}
