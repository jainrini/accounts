package com.mb.module.service;

import com.mb.module.dao.AccountDao;
import com.mb.module.dto.AccountCreationDto;
import com.mb.module.dto.AccountDto;
import com.mb.module.dto.BalanceDto;
import com.mb.module.enums.TransactionCurrency;
import com.mb.module.exceptions.AccountNotFoundException;
import com.mb.module.exceptions.ApiException;
import com.mb.module.queue.MessageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountDao accountDao;
    @Mock
    private BalanceService balanceService;
    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void createAccount_CreatesBalances_WithListOfCurrencies() throws ApiException {
        List<TransactionCurrency> currency = Arrays.asList(TransactionCurrency.EUR, TransactionCurrency.SEK);
        AccountCreationDto accountRequest = AccountCreationDto.builder()
            .countryCode("US")
            .currencies(currency)
            .customerId(1)
            .build();

        accountService.createAccount(accountRequest);
        verify(accountDao).insert(accountRequest);
        verify(balanceService, times(2)).createBalance(any(BalanceDto.class));

        Integer accountId = BalanceDto
            .builder()
            .customerId(1)
            .countryCode("US")
            .build()
            .getAccountId();

        verify(balanceService).findByAccountId(accountId);
        verify(messageSender).sendAccountCreationMessage(any(AccountDto.class));
    }

    @Test
    public void getAccountById_Returns_AccountInfoWithBalances() throws ApiException, AccountNotFoundException {
        Integer accountId = Integer.valueOf(1);
        BalanceDto balance = BalanceDto.builder()
            .accountId(accountId)
            .balanceAmount(BigDecimal.ZERO)
            .customerId(1)
            .currencyCode(TransactionCurrency.USD)
            .countryCode("US")
            .build();
        AccountCreationDto account = AccountCreationDto
            .builder()
            .id(accountId)
            .customerId(1)
            .countryCode("US")
            .build();
        when(accountDao.findByAccountId(accountId)).thenReturn(Optional.ofNullable(account));
        when(balanceService.findByAccountId(accountId)).thenReturn(Arrays.asList(balance));
        AccountDto accountResponse = accountService.getAccountById(accountId);
        assertEquals(accountId, accountResponse.getId());
        assertEquals(1, accountResponse.getBalances().size());
    }

    @Test
    public void getAccountById_Returns_ptionExce() throws ApiException, AccountNotFoundException {
        Integer accountId = Integer.valueOf(1);

        when(accountDao.findByAccountId(accountId)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> accountService.getAccountById(accountId));

        assertThat(thrown)
            .isInstanceOf(AccountNotFoundException.class)
            .hasMessage("Account id 1 not found");

    }

}

