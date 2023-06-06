package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponceDTO {
    private String fromAccount;
    private String toAccount;
    private float amount;
    private String description;
    private LocalDateTime timestamp;

    public TransactionResponceDTO(String fromAccount, String toAccount, float amount, String description, LocalDateTime timestamp) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }

    public TransactionResponceDTO() {

    }

}
