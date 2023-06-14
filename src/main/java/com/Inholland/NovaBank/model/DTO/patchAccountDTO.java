package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class patchAccountDTO extends BaseDTO {
    private String iban;
    private String op;
    private String key;
    private String value;

    public patchAccountDTO(String iban, String op, String key, String value) {
        this.iban = iban;
        this.op = op;
        this.key = key;
        this.value = value;
    }

    public patchAccountDTO() {

    }
}
