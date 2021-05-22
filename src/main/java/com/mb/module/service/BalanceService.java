package com.mb.module.service;

import com.mb.module.dao.BalanceDao;
import com.mb.module.dto.BalanceDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceService {

    private BalanceDao balanceDao;

    public BalanceService(BalanceDao balanceDao) {
        this.balanceDao = balanceDao;
    }

    public BalanceDto getByAccountIdAndCurrency(Integer accountId, String currencyCode) {
        BalanceDto balance = balanceDao.findByAccountIdAndCurrency(accountId, currencyCode);
        return balance;
    }

    public void updateBalanceAmount(BigDecimal newBalanceAmount,
                                    Integer balanceId) {
        balanceDao.update(newBalanceAmount, balanceId);
    }

    public void getByAccountId(Integer accountId) {
        balanceDao.findByAccountId(accountId);
    }
}
