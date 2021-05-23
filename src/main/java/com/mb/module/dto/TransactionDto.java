package com.mb.module.dto;

import com.mb.module.dto.validator.ValidateCurrency;
import com.mb.module.dto.validator.ValidateDirectionCode;
import com.mb.module.enums.DirectionCode;
import com.mb.module.enums.TransactionCurrency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import static com.mb.module.enums.DirectionCode.IN;
import static com.mb.module.enums.DirectionCode.OUT;
import static com.mb.module.enums.TransactionCurrency.EUR;
import static com.mb.module.enums.TransactionCurrency.GBP;
import static com.mb.module.enums.TransactionCurrency.SEK;
import static com.mb.module.enums.TransactionCurrency.USD;

@Data
@Builder
public class TransactionDto {

    private Integer id;
    private Integer accountId;
    private BigDecimal amount;
    @ValidateCurrency(anyOf = { EUR, SEK, GBP, USD })
    private TransactionCurrency currencyCode;
    @ValidateDirectionCode(anyOf = { IN, OUT })
    private DirectionCode directionCode;
    private String description;
    private BigDecimal initialBalanceAmount;
    private BigDecimal balanceAfterTransaction;
}
