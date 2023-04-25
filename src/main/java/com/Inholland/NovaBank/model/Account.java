package com.Inholland.NovaBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Data;


@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private long id;
    private String iban;
    private float balance;

    private int user;
    private AccountType accountType;
    private String currency;
    private boolean status;

}
