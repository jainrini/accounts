package com.mb.module.dto;

import com.mb.module.dto.validator.ValidateCurrency;
import com.mb.module.dto.validator.ValidateDirectionCode;
import com.mb.module.enums.DirectionCode;
import com.mb.module.enums.TransactionCurrency;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.mb.module.enums.DirectionCode.IN;
import static com.mb.module.enums.DirectionCode.OUT;
import static com.mb.module.enums.TransactionCurrency.EUR;
import static com.mb.module.enums.TransactionCurrency.GBP;
import static com.mb.module.enums.TransactionCurrency.SEK;
import static com.mb.module.enums.TransactionCurrency.USD;

@Data
@Builder
@ApiModel
public class TransactionCreationDto {

    @NotNull
    private Integer accountId;
    @NotNull
    private BigDecimal amount;
    @ValidateCurrency(anyOf = { EUR, SEK, GBP, USD })
    private TransactionCurrency currencyCode;
    @ValidateDirectionCode(anyOf = { IN, OUT })
    private DirectionCode directionCode;
    @NotNull
    private String description;
}
