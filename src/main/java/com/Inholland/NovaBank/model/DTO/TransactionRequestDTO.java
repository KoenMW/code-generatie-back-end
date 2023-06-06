package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    private String fromAccount;
    private String toAccount;
    private float amount;
    private String description;

    public TransactionRequestDTO(String fromAccount, String toAccount, float amount, String description) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
    }

    public TransactionRequestDTO() {

    }

}
