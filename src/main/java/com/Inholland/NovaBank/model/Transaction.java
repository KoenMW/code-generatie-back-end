package com.Inholland.NovaBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
@Table(name = "\"Transactions\"")
public class Transaction {
    @Id
    @GeneratedValue
    private int id;

    private LocalDateTime timestamp;
    private String fromAccount;
    private String toAccount;
    private float amount;
    private String description;
    private long userId;

    public Transaction(LocalDateTime timestamp, String fromAccount, String toAccount, float amount, String description, long userId) {
        this.timestamp = timestamp;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.userId = userId;
    }

    public Transaction() {

    }
}

