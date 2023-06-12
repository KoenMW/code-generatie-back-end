package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.ErrorDTO;
import org.junit.jupiter.api.Test;

public class ErrorDTOTest {

    @Test
    public void testConstructor() {
        ErrorDTO errorDTO = new ErrorDTO("message", 1);
        assert(errorDTO.getMessage().equals("message"));
        assert(errorDTO.getCode() == 1);
    }

    @Test
    public void testConstructor2() {
        ErrorDTO errorDTO = new ErrorDTO();
        assert(errorDTO.getMessage() == null);
        assert(errorDTO.getCode() == 0);
    }
}
