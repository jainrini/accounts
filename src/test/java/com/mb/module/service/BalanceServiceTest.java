package com.mb.module.service;

import com.mb.module.dao.BalanceDao;
import com.mb.module.dto.BalanceDto;
import com.mb.module.enums.TransactionCurrency;
import com.mb.module.exceptions.ApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BalanceServiceTest {

    @Mock
    private BalanceDao balanceDao;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    public void createBalance() {
        BalanceDto balanceDto = BalanceDto.builder()
            .balanceAmount(BigDecimal.TEN)
            .customerId(1)
            .currencyCode(TransactionCurrency.USD)
            .build();

        balanceService.createBalance(balanceDto);
        verify(balanceDao).createBalance(balanceDto);
    }

    @Test
    public void getByAccountIdAndCurrency_ReturnsBalance_ForAccountIdAndCurrency() {
        Integer accountId = 1;

        BalanceDto balance = BalanceDto.builder()
            .balanceAmount(BigDecimal.TEN)
            .customerId(1)
            .accountId(accountId)
            .currencyCode(TransactionCurrency.SEK)
            .build();
        when(balanceDao.findByAccountIdAndCurrency(accountId, TransactionCurrency.SEK.name()))
            .thenReturn(java.util.Optional.ofNullable(balance));
        BalanceDto balances = balanceService.getByAccountIdAndCurrency(accountId, TransactionCurrency.SEK.name());
        assertEquals(accountId, balances.getAccountId());
    }

    @Test
    public void getByAccountIdAndCurrency_ThrowsBalanceNotFound_IfBalanceDoesnotExist() {
        Integer accountId = 1;

        BalanceDto balance = BalanceDto.builder()
            .balanceAmount(BigDecimal.TEN)
            .customerId(1)
            .accountId(accountId)
            .currencyCode(TransactionCurrency.SEK)
            .build();
        when(balanceDao.findByAccountIdAndCurrency(accountId, TransactionCurrency.SEK.name()))
            .thenReturn(Optional.empty());
        Throwable thrown = catchThrowable(() -> balanceService.getByAccountIdAndCurrency(accountId, TransactionCurrency.SEK.name()));
        assertThat(thrown)
            .isInstanceOf(ApiException.class)
            .hasMessage("Balance account id 1 for currency SEK don't exist");
    }

    @Test
    public void findByAccountId_ReturnsBalances_ForGivenAccountId() {
        Integer accountId = 1;

        BalanceDto balance1 = BalanceDto.builder()
            .balanceAmount(BigDecimal.TEN)
            .customerId(1)
            .accountId(accountId)
            .currencyCode(TransactionCurrency.SEK)
            .build();
        BalanceDto balance2 = BalanceDto.builder()
            .balanceAmount(BigDecimal.TEN)
            .customerId(1)
            .accountId(accountId)
            .currencyCode(TransactionCurrency.USD)
            .build();

        List<BalanceDto> balanceDtos = Arrays.asList(balance1, balance2);
        when(balanceDao.findByAccountId(accountId))
            .thenReturn(balanceDtos);
        List<BalanceDto> balances = balanceService.findByAccountId(accountId);
        assertEquals(2, balances.size());
    }

    @Test
    public void createBalance_ThrowsDuplicateException_IfBalanceAlreadyExist() {
        BalanceDto balance1 = BalanceDto.builder()
            .balanceAmount(BigDecimal.TEN)
            .customerId(1)
            .countryCode("US")
            .accountId(1)
            .currencyCode(TransactionCurrency.USD)
            .build();

        doThrow(DuplicateKeyException.class)
            .when(balanceDao)
            .createBalance(balance1);

        Throwable throwable = catchThrowable(() -> balanceService.createBalance(balance1));
        assertThat(throwable)
            .isInstanceOf(ApiException.class)
            .hasMessage("Balance for currency USD already exist for customer 1 and country US");

    }
}
