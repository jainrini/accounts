package com.mb.module.service;

import com.mb.module.dao.BalanceDao;
import com.mb.module.dto.BalanceDto;
import com.mb.module.exceptions.ApiException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    public int updateBalanceAmount(BigDecimal newBalanceAmount,
                                   Integer balanceId) {
        return balanceDao.update(newBalanceAmount, balanceId);
    }

    public List<BalanceDto> findByAccountId(Integer accountId) {
        return balanceDao.findByAccountId(accountId);
    }

    public void createBalance(BalanceDto balanceDto) {
        try {
            balanceDao.createBalance(balanceDto);
        } catch (DuplicateKeyException ex) {
            throw new ApiException(
                format("Balance for currency %s already exist for customer %s and country %s",
                    balanceDto.getCurrencyCode(),
                    balanceDto.getCustomerId(),
                    balanceDto.getCountryCode()
                )
            );
        }
    }
}
