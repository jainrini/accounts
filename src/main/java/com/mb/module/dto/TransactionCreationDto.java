package com.mb.module.dto;

import com.mb.module.dto.validator.ValidateCurrency;
import com.mb.module.dto.validator.ValidateDirectionCode;
import com.mb.module.enums.DirectionCode;
import com.mb.module.enums.TransactionCurrency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
public class TransactionCreationDto {

    @NotNull
    private Integer accountId;
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal amount;
    @ValidateCurrency(anyOf = { EUR, SEK, GBP, USD })
    private TransactionCurrency currencyCode;
    @ValidateDirectionCode(anyOf = { IN, OUT })
    private DirectionCode directionCode;
    @NotNull(message = "Description is mandatory field")
    @NotEmpty(message = "Description cannot be empty")
    private String description;
}
