package com.gladkiei.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyRequestDTO {
    private String code;
    private String name;
    private String sign;

    public CurrencyRequestDTO(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
