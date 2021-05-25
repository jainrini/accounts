package com.mb.module.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mb.module.dto.validator.ValidateCountryCode;
import com.mb.module.enums.TransactionCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationDto {

    @JsonIgnore
    private Integer id;

    @NotNull(message = "Customer Id cannot be empty")
    private Integer customerId;

    @NotNull
    @ValidateCountryCode
    private String countryCode;

    @NotEmpty(message = "Currency cannot be empty.")
    private List<TransactionCurrency> currencies;

}
