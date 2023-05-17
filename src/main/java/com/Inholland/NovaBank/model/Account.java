package com.Inholland.NovaBank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@DynamicUpdate
@Data
public class Account {
    @Id
    private String iban;
    private double balance;
    private long userReferenceId;
    private AccountType accountType;
    private boolean active;
    private float absoluteLimit;

    public Account(String iban, double balance, long userId, AccountType accountType, String currency, boolean status, float absoluteLimit) {
        this.iban = iban;
        this.balance = balance;
        this.userReferenceId = userId;
        this.accountType = accountType;
        this.active = status;
        this.absoluteLimit = absoluteLimit;
    }

    public Account() {

    }

}
