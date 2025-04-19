package com.gladkiei.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyResponseDTO {
    private int id;
    private String code;
    private String name;
    private String sign;

    public CurrencyResponseDTO(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
