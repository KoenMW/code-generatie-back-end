package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class TransactionRequestDTO extends BaseDTO {
    private String fromAccount;
    private String toAccount;
    private float amount;
    private String description;
    private long userId;

    public TransactionRequestDTO(String fromAccount, String toAccount, float amount, String description, long userId) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.userId = userId;
    }

    public TransactionRequestDTO() {

    }

}
