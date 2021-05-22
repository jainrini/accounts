package com.mb.module.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AccountCreationDto {

    private Integer id;
    @NotNull
    private Integer customerId;
    @NotNull
    private String countryCode;
    @NotEmpty(message = "Currency cannot be empty.")
    private List<String> currencies;

}
