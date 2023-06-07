package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class DepositWithdrawDTO extends BaseDTO {
    private String iban;
    private float amount;

    public DepositWithdrawDTO(String iban, float amount) {
        this.iban = iban;
        this.amount = amount;
    }

    public DepositWithdrawDTO() {

    }

    public String getIban() {
        return iban;
    }

    public float getAmount() {
        return amount;
    }
}
