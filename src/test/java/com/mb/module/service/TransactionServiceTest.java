package com.mb.module.service;

import com.mb.module.dao.TransactionDao;
import com.mb.module.dto.AccountCreationDto;
import com.mb.module.dto.BalanceDto;
import com.mb.module.dto.TransactionCreationDto;
import com.mb.module.dto.TransactionDto;
import com.mb.module.enums.DirectionCode;
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

import static com.mb.module.enums.TransactionCurrency.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private TransactionDao transactionDao;
    @Mock
    private AccountService accountService;
    @Mock
    private BalanceService balanceService;

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void createTransaction_CreateTransactionAndReturnBalance_ForAccountId() throws AccountNotFoundException {
        TransactionCreationDto transactionReq = TransactionCreationDto.builder()
            .accountId(1)
            .currencyCode(USD)
            .directionCode(DirectionCode.IN)
            .description("Test")
            .amount(BigDecimal.TEN)
            .build();

        TransactionDto transactionResponse = TransactionDto.builder()
            .accountId(1)
            .currencyCode(USD)
            .directionCode(DirectionCode.IN)
            .description("Test")
            .amount(BigDecimal.TEN)
            .balanceAfterTransaction(BigDecimal.TEN)
            .initialBalanceAmount(BigDecimal.ZERO)
            .build();

        BalanceDto initialBalance = BalanceDto.builder()
            .id(1)
            .balanceAmount(BigDecimal.ZERO)
            .customerId(1)
            .countryCode("US")
            .accountId(1)
            .currencyCode(TransactionCurrency.USD)
            .build();

        when(accountService.getAccountId(1)).
            thenReturn(AccountCreationDto.builder().id(1).build());
        when(balanceService.getByAccountIdAndCurrency(
            1,
            USD.name()
        )).thenReturn(initialBalance);
        transactionService.createTransaction(transactionReq);

        verify(balanceService).updateBalanceAmount(BigDecimal.TEN, 1);
        verify(transactionDao).insert(transactionResponse);
        verify(messageSender).sendTransactionCreatedMessage(transactionResponse);
    }

    @Test
    public void createTransactionOut_CreateTransactionAndReturnBalance_ForAccountId() throws AccountNotFoundException {
        TransactionCreationDto transactionReq = TransactionCreationDto.builder()
            .accountId(1)
            .currencyCode(USD)
            .directionCode(DirectionCode.OUT)
            .description("Test")
            .amount(BigDecimal.ONE)
            .build();

        TransactionDto transactionResponse = TransactionDto.builder()
            .accountId(1)
            .currencyCode(USD)
            .directionCode(DirectionCode.OUT)
            .description("Test")
            .amount(BigDecimal.ONE)
            .balanceAfterTransaction(BigDecimal.valueOf(9))
            .initialBalanceAmount(BigDecimal.TEN)
            .build();

        BalanceDto initialBalance = BalanceDto.builder()
            .id(1)
            .balanceAmount(BigDecimal.TEN)
            .customerId(1)
            .countryCode("US")
            .accountId(1)
            .currencyCode(TransactionCurrency.USD)
            .build();

        when(accountService.getAccountId(1)).
            thenReturn(AccountCreationDto.builder().id(1).build());
        when(balanceService.getByAccountIdAndCurrency(
            1,
            USD.name()
        )).thenReturn(initialBalance);
        transactionService.createTransaction(transactionReq);

        verify(balanceService).updateBalanceAmount(BigDecimal.valueOf(9), 1);
        verify(transactionDao).insert(transactionResponse);
        verify(messageSender).sendTransactionCreatedMessage(transactionResponse);
    }

    @Test
    public void getTransactionByAccountId_ReturnsTransaction_ForGivenAccountId() throws AccountNotFoundException {
        Integer accountId = 1;
        AccountCreationDto account = AccountCreationDto
            .builder()
            .id(accountId)
            .customerId(1)
            .countryCode("US")
            .build();

        TransactionDto transactionResponse = TransactionDto.builder()
            .accountId(1)
            .currencyCode(USD)
            .directionCode(DirectionCode.IN)
            .description("Test")
            .amount(BigDecimal.ONE)
            .balanceAfterTransaction(BigDecimal.valueOf(9))
            .initialBalanceAmount(BigDecimal.TEN)
            .build();

        when(accountService.getAccountId(accountId)).thenReturn(account);
        when(transactionDao.findByAccountId(accountId)).thenReturn(Arrays.asList(transactionResponse));

        List<TransactionDto> transactionByAccountId = transactionService.getTransactionByAccountId(accountId);
        assertEquals(accountId, transactionByAccountId.get(0).getAccountId());
    }

    @Test
    public void createTransactionOut_ThrowsInsufficientBalance_IfAmountIsLessThanAvailable() throws AccountNotFoundException {
        TransactionCreationDto transactionReq = TransactionCreationDto.builder()
            .accountId(1)
            .currencyCode(USD)
            .directionCode(DirectionCode.OUT)
            .description("Test")
            .amount(BigDecimal.TEN)
            .build();

        BalanceDto initialBalance = BalanceDto.builder()
            .id(1)
            .balanceAmount(BigDecimal.ONE)
            .customerId(1)
            .countryCode("US")
            .accountId(1)
            .currencyCode(TransactionCurrency.USD)
            .build();

        when(accountService.getAccountId(1)).
            thenReturn(AccountCreationDto.builder().id(1).build());
        when(balanceService.getByAccountIdAndCurrency(
            1,
            USD.name()
        )).thenReturn(initialBalance);

        Throwable throwable = catchThrowable(() -> transactionService.createTransaction(transactionReq));
        assertThat(throwable)
            .isInstanceOf(ApiException.class)
            .hasMessage("Insufficient funds in account 1");
    }

}
