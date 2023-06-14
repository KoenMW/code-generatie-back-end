package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.newUserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class newUserDTOTest {
    @Test
    void testConstructor() {
        newUserDTO actualNewUserDTO = new newUserDTO("firstName", "lastName", "username", "password", "email");
        assertNotNull(actualNewUserDTO);
    }
    @Test
    void testConstructor2() {
        newUserDTO actualNewUserDTO = new newUserDTO();
        assertNotNull(actualNewUserDTO);
    }
}
