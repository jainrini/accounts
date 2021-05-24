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
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.mb.module.config.MessagingConfig.EXCHANGE;
import static com.mb.module.config.MessagingConfig.ROUTING_KEY;
import static java.lang.String.format;

@Service
@AllArgsConstructor
@Transactional
public class TransactionService {

    private final BalanceService balanceService;
    private final TransactionDao transactionDao;
    private final AccountService accountService;
    private final RabbitTemplate rabbitTemplate;

    public TransactionDto createTransaction(TransactionCreationDto transaction) throws AccountNotFoundException {
        TransactionDto transactionDto = buildTransactionDto(transaction);
        AccountCreationDto account = accountService.getAccountId(transaction.getAccountId());

        BalanceDto balance = getExistingBalance(transaction.getCurrencyCode(), account.getId());
        return makeTransaction(transactionDto, balance);
    }

    private TransactionDto makeTransaction(TransactionDto transactionDto, BalanceDto balance) {
        BigDecimal initialBalanceAmount = balance.getBalanceAmount();
        TransactionDto transaction;
        switch (transactionDto.getDirectionCode()) {
            case IN:
                transaction = createTransactionIn(initialBalanceAmount, transactionDto, balance);
                break;
            case OUT:
                transaction = createTransactionOut(initialBalanceAmount, transactionDto, balance);
                break;
            default:
                throw new ApiException("Not valid transaction");
        }
        return transaction;
    }

    private void publishTransactionsToQueue(TransactionDto transaction) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, transaction);
    }

    private TransactionDto createTransactionOut(BigDecimal initialBalanceAmount, TransactionDto transactionDto, BalanceDto balance) {
        BigDecimal amount = transactionDto.getAmount();
        if (initialBalanceAmount.compareTo(amount) < 0) {
            throw new ApiException(format("Insufficient funds in account %s", transactionDto.getAccountId()));
        }
        BigDecimal newBalanceAmount = initialBalanceAmount.subtract(amount);
        return updateBalanceAndCreateTransaction(
            transactionDto,
            initialBalanceAmount,
            balance,
            newBalanceAmount);
    }

    private TransactionDto createTransactionIn(BigDecimal initialBalanceAmount, TransactionDto transactionDto, BalanceDto balance) {
        BigDecimal newBalanceAmount = initialBalanceAmount.add(transactionDto.getAmount());
        return updateBalanceAndCreateTransaction(
            transactionDto,
            initialBalanceAmount,
            balance,
            newBalanceAmount
        );
    }

    private TransactionDto updateBalanceAndCreateTransaction(
        TransactionDto transactionDto,
        BigDecimal initialBalanceAmount,
        BalanceDto balance,
        BigDecimal newBalanceAmount
    ) {
        balanceService.updateBalanceAmount(newBalanceAmount, balance.getId());
        transactionDto.setInitialBalanceAmount(initialBalanceAmount);
        transactionDto.setBalanceAfterTransaction(newBalanceAmount);
        transactionDao.insert(transactionDto);
        return transactionDto;
    }

    private BalanceDto getExistingBalance(TransactionCurrency currency, Integer accountId) {
        return balanceService.getByAccountIdAndCurrency(
            accountId,
            currency.name()
        );
    }

    private TransactionDto buildTransactionDto(TransactionCreationDto transaction) {
        return TransactionDto.builder()
            .accountId(transaction.getAccountId())
            .currencyCode(transaction.getCurrencyCode())
            .amount(transaction.getAmount())
            .description(transaction.getDescription())
            .directionCode(transaction.getDirectionCode())
            .build();
    }

    private boolean isTransactionInValid(TransactionDto transaction) {
        return transaction.getDirectionCode() == DirectionCode.IN;
    }

    private boolean isOutTransactionValid(TransactionDto transaction,
                                          BigDecimal initialBalanceAmount
    ) {
        return transaction.getDirectionCode() == DirectionCode.OUT
            && isValidAmount(transaction.getAmount(), initialBalanceAmount);
    }

    private boolean isValidAmount(BigDecimal amountDuringTransaction, BigDecimal initialBalanceAmount) {
        if (!(amountDuringTransaction.compareTo(initialBalanceAmount) <= 0)) {
            throw new ApiException("Insufficient Balance");
        }
        return false;
    }

    public List<TransactionDto> getTransactionById(Integer accountId) throws AccountNotFoundException {
        AccountCreationDto account = accountService.getAccountId(accountId);
        return transactionDao.findById(account.getId());
    }
}
