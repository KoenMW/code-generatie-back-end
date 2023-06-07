package com.Inholland.NovaBank.model.DTO;

import com.Inholland.NovaBank.model.AccountType;
import lombok.Data;


@Data
public class newAccountDTO extends BaseDTO {
    private long userReferenceId;
    private AccountType accountType;
    private float absoluteLimit;

    public newAccountDTO(long userReferenceId, AccountType accountType, float absoluteLimit) {
        this.userReferenceId = userReferenceId;
        this.accountType = accountType;
        this.absoluteLimit = absoluteLimit;
    }

    public newAccountDTO() {

    }
}
