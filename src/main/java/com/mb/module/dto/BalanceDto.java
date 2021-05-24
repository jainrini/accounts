package com.mb.module.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mb.module.enums.TransactionCurrency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BalanceDto {

    private Integer id;
    @JsonIgnore
    private Integer customerId;
    @JsonIgnore
    private Integer accountId;
    private String countryCode;
    private TransactionCurrency currencyCode;
    private BigDecimal balanceAmount;

}
