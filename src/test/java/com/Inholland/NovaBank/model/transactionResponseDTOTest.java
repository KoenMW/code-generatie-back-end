package com.Inholland.NovaBank.model;

import com.Inholland.NovaBank.model.DTO.TransactionResponceDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class transactionResponseDTOTest {
    @Test
    void testConstructor(){
        TransactionResponceDTO actualTransactionResponseDTO = new TransactionResponceDTO();
        assertNotNull(actualTransactionResponseDTO);
    }

}
