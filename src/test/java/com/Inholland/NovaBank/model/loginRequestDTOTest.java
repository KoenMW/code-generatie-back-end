package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import org.junit.jupiter.api.Test;

public class loginRequestDTOTest {
    @Test
    public void testConstructor() {
        // Arrange and Act
        LoginRequestDTO actualLoginRequestDTO = new LoginRequestDTO("janedoe", "iloveyou");
        // Assert
        assert actualLoginRequestDTO != null;
        assert actualLoginRequestDTO.getPassword().equals("iloveyou");
        assert actualLoginRequestDTO.getUsername().equals("janedoe");

    }

    @Test
    public void testConstructor2() {
        // Arrange and Act
        LoginRequestDTO actualLoginRequestDTO = new LoginRequestDTO();
        // Assert
        assert actualLoginRequestDTO != null;
        assert actualLoginRequestDTO.getPassword() == null;
        assert actualLoginRequestDTO.getUsername() == null;

    }
}
