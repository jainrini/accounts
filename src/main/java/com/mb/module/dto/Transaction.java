package com.mb.module.dto;

import com.mb.module.enums.DirectionCode;
import com.mb.module.enums.TransactionCurrency;
import lombok.Data;

@Data
public class Transaction {

    private Integer transactionId;
    private Integer accountId;
    private Double amountDuringTransaction;
    private TransactionCurrency currency;
    private DirectionCode directionCode;
    private String description;
    private Double balanceAfterTransaction;
}
