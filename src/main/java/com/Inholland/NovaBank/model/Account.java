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
    @ManyToOne
    //@JsonIgnore
    private User user;
    private AccountType accountType;
    private String currency;
    private boolean status;
    private float absoluteLimit;

    public Account(String iban, float balance, long userId, User user,  AccountType accountType, String currency, boolean status, float absoluteLimit) {
        this.iban = iban;
        this.balance = balance;
        this.userReferenceId = userId;
        this.user = user;
        this.accountType = accountType;
        this.currency = currency;
        this.status = status;
        this.absoluteLimit = absoluteLimit;
    }

    public Account() {

    }

}
