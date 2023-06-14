package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.searchAccountDTO;
import org.junit.jupiter.api.Test;

public class searchAccountDTOTest {
    @Test
    public void testConstructor() {
        // Arrange and Act
        searchAccountDTO actualSearchAccountDTO = new searchAccountDTO("Iban", 1L, AccountType.CHECKING);

        // Assert
        assert actualSearchAccountDTO.getIban().equals("Iban");
        assert actualSearchAccountDTO.getUserReferenceId() == 1L;
        assert actualSearchAccountDTO.getAccountType() == AccountType.CHECKING;
    }

    @Test
    public void testConstructor2() {
        // Arrange and Act
        searchAccountDTO actualSearchAccountDTO = new searchAccountDTO();

        // Assert
        assert actualSearchAccountDTO.getIban() == null;
        assert actualSearchAccountDTO.getUserReferenceId() == 0L;
        assert actualSearchAccountDTO.getAccountType() == null;
    }

}
