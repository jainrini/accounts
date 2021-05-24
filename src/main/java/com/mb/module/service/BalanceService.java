package com.mb.module.service;

import com.mb.module.dao.BalanceDao;
import com.mb.module.dto.BalanceDto;
import com.mb.module.exceptions.ApiException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.String.format;

@Service
public class BalanceService {

    private BalanceDao balanceDao;

    public BalanceService(BalanceDao balanceDao) {
        this.balanceDao = balanceDao;
    }

    public BalanceDto getByAccountIdAndCurrency(Integer accountId, String currencyCode) {
        return balanceDao.findByAccountIdAndCurrency(accountId, currencyCode)
            .orElseThrow(() -> new ApiException(
                format("Balance account id %s for currency %s don't exist", accountId, currencyCode)
            ));
    }

    public void updateBalanceAmount(BigDecimal newBalanceAmount,
                                    Integer balanceId) {
        balanceDao.update(newBalanceAmount, balanceId);
    }
}
