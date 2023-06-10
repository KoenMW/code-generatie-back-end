package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.LoginResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class loginResponseDTOTest {
    @Test
    public void testConstructor() {
        // Arrange and Act
        LoginResponseDTO actualLoginResponseDTO = new LoginResponseDTO();
        // Assert
        assertNull(actualLoginResponseDTO.getToken());

    }

    @Test
    public void testConstructor2(){
        // Arrange and Act
        LoginResponseDTO actualLoginResponseDTO = new LoginResponseDTO("qiueqiuowgeiugeioqgehqjwe");
        // Assert
        assertNotNull(actualLoginResponseDTO);
        assertNotNull(actualLoginResponseDTO.getToken());
    }
}
