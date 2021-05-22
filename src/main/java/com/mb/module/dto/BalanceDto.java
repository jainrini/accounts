package com.mb.module.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDto {

    private Integer id;
    @JsonIgnore
    private Integer accountId;
    @JsonIgnore
    private Integer customerId;
    private BigDecimal balanceAmount;
    private String currencyCode;
}
