package com.mb.module.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountDto {

    private Integer id;
    private Integer customerId;
    private List<BalanceDto> balanceDtoList;
}
