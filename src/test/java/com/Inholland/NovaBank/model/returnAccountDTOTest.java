package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import org.junit.jupiter.api.Test;

public class returnAccountDTOTest {
    @Test
    public void testConstructor() {
        // Arrange and Act
        returnAccountDTO actualReturnAccountDTO = new returnAccountDTO("Iban", AccountType.CHECKING);

        // Assert
        assert actualReturnAccountDTO.getIban().equals("Iban");
        assert actualReturnAccountDTO.getAccountType().equals(AccountType.CHECKING);
    }

    @Test
    public void testConstructor2() {
        // Arrange and Act
        returnAccountDTO actualReturnAccountDTO = new returnAccountDTO();

        // Assert
        assert actualReturnAccountDTO.getIban() == null;
        assert actualReturnAccountDTO.getAccountType() == null;
    }


}
