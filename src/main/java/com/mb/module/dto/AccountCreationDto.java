package com.mb.module.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mb.module.enums.TransactionCurrency;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AccountCreationDto {

    @JsonIgnore
    private Integer id;

    @NotNull(message = "Customer Id cannot be empty")
    private Integer customerId;

    @NotNull
    private String countryCode;

    @NotEmpty(message = "Currency cannot be empty.")
    private List<TransactionCurrency> currencies;

}
