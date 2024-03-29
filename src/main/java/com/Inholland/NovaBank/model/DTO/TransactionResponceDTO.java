package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponceDTO extends BaseDTO {
    private String fromAccount;
    private String toAccount;
    private float amount;
    private String description;
    private LocalDateTime timestamp;
    private String direction;

    public TransactionResponceDTO(String fromAccount, String toAccount, float amount, String description, LocalDateTime timestamp, String direction) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.direction = direction;
    }

    public TransactionResponceDTO() {

    }

}
