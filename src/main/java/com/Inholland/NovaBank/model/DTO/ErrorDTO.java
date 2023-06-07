package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class ErrorDTO extends BaseDTO{
    private String message;
    private String code;

    public ErrorDTO(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public ErrorDTO() {

    }
}
