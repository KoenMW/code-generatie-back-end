package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.patchUserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class patchUserDTOTest {
    @Test
    void testConstructor() {
        patchUserDTO actualPatchUserDTO = new patchUserDTO(1l, "op", "key", "value");
        assertNotNull(actualPatchUserDTO);
    }
    @Test
    void testConstructor2() {
        patchUserDTO actualPatchUserDTO = new patchUserDTO();
        assertNotNull(actualPatchUserDTO);
    }
}
