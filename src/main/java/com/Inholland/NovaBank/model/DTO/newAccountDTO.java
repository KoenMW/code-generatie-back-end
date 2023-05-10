package com.Inholland.NovaBank.model.DTO;

import com.Inholland.NovaBank.model.AccountType;
import jakarta.persistence.Entity;
import lombok.Data;


@Data
public class newAccountDTO {
    private long userId;
    private AccountType accountType;
    private float absoluteLimit;

    public newAccountDTO(long userId, AccountType accountType, float absoluteLimit) {
        this.userId = userId;
        this.accountType = accountType;
        this.absoluteLimit = absoluteLimit;
    }

    public newAccountDTO() {

    }
}
