package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.returnUserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class returnUserDTOTest {
    @Test
    void testConstructor() {
        returnUserDTO actualReturnUserDTO = new returnUserDTO(1l, "First Name", "Last Name", "janedoe", "jane@doe.nl", Role.ROLE_USER, 1, 1, true);
        assertNotNull(actualReturnUserDTO);
    }
    @Test
    void testConstructor2() {
        returnUserDTO actualReturnUserDTO = new returnUserDTO();
        assertNotNull(actualReturnUserDTO);
    }
}
