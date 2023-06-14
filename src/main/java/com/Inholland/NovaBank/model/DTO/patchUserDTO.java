package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class patchUserDTO  extends BaseDTO{
    private Long id;
    private String op;
    private String key;
    private String value;

    public patchUserDTO(Long id, String op, String key, String value) {
        this.id = id;
        this.op = op;
        this.key = key;
        this.value = value;
    }
    public patchUserDTO() { }
}
