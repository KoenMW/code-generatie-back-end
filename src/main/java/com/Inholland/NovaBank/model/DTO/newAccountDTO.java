package com.Inholland.NovaBank.model.DTO;

import com.Inholland.NovaBank.model.AccountType;
import lombok.Data;


@Data
public class newAccountDTO {
    private long userReferenceId;
    private AccountType accountType;
    private float absoluteLimit;
    private float dailyLimit;
    private float transactionLimit;

    public newAccountDTO(long userReferenceId, AccountType accountType, float absoluteLimit, float dailyLimit, float transactionLimit) {
        this.userReferenceId = userReferenceId;
        this.accountType = accountType;
        this.absoluteLimit = absoluteLimit;
        this.dailyLimit = dailyLimit;
        this.transactionLimit = transactionLimit;
    }

    public newAccountDTO() {

    }
}
