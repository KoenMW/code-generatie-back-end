package com.Inholland.NovaBank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;


@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private long id;
    private String iban;
    private float balance;
    private long userReferenceId;
    private AccountType accountType;
    private boolean isActive;
    private float absoluteLimit;

    public Account(String iban, float balance, long userId, AccountType accountType, boolean status, float absoluteLimit) {
        this.iban = iban;
        this.balance = balance;
        this.userReferenceId = userId;
        this.accountType = accountType;
        this.isActive = status;
        this.absoluteLimit = absoluteLimit;
    }

    public Account() {

    }

}
