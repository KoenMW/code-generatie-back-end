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
    private double amount;
    private String description;
}
