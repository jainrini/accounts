package com.mb.module.dto;

import com.mb.module.dto.validator.ValidateCurrency;
import com.mb.module.dto.validator.ValidateDirectionCode;
import com.mb.module.enums.DirectionCode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.mb.module.enums.DirectionCode.IN;
import static com.mb.module.enums.DirectionCode.OUT;
import static com.mb.module.enums.TransactionCurrency.EUR;
import static com.mb.module.enums.TransactionCurrency.GBP;
import static com.mb.module.enums.TransactionCurrency.SEK;
import static com.mb.module.enums.TransactionCurrency.USD;

@Data
public class Transaction {

    private Integer id;
    private Integer accountId;
    private BigDecimal amount;
    @ValidateCurrency(anyOf = { EUR, SEK, GBP, USD })
    private String currencyCode;
    @ValidateDirectionCode(anyOf = { IN, OUT })
    private DirectionCode directionCode;
    @NotNull
    private String description;
    private BigDecimal initialBalanceAmount;
    private BigDecimal balanceAfterTransaction;
}
