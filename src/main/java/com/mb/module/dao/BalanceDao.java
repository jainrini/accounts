package com.mb.module.dao;

import com.mb.module.dto.BalanceDto;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Mapper
public interface BalanceDao {

    void createBalance(BalanceDto balanceDtos);

    List<BalanceDto> findByAccountId(Integer accountId);

    Optional<BalanceDto> findByAccountIdAndCurrency(Integer accountId, String currencyCode);

    int update(BigDecimal newBalanceAmount, Integer balanceId);
}
