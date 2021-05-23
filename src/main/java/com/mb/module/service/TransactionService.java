package com.mb.module.service;

import com.mb.module.dao.TransactionDao;
import com.mb.module.dto.BalanceDto;
import com.mb.module.dto.TransactionCreationDto;
import com.mb.module.dto.TransactionDto;
import com.mb.module.enums.DirectionCode;
import com.mb.module.enums.TransactionCurrency;
import com.mb.module.exceptions.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final BalanceService balanceService;
    private final TransactionDao transactionDao;
    private final AccountService accountService;

    public TransactionService(BalanceService balanceService, TransactionDao transactionDao, AccountService accountService) {
        this.balanceService = balanceService;
        this.transactionDao = transactionDao;
        this.accountService = accountService;
    }

    public TransactionDto createTransaction(TransactionCreationDto transaction) {
        TransactionDto transactionDto = buildTransactionDto(transaction);

        Integer accountId = transactionDto.getAccountId();
        if (!accountService.isValidAccountId(accountId)) {
            throw new ApiException("AccountId not found");
        }
        BalanceDto balance = getExistingBalance(transaction.getCurrencyCode(), accountId);
        return makeTransaction(transactionDto, balance);
    }

    private TransactionDto makeTransaction(TransactionDto transactionDto, BalanceDto balance) {
        BigDecimal initialBalanceAmount = balance.getBalanceAmount();
        BigDecimal amountDuringTransaction = transactionDto.getAmount();

        if (isTransactionInValid(transactionDto)) {
            return createTransactionIn(initialBalanceAmount, transactionDto, balance);
        } else if (isOutTransactionValid(transactionDto, initialBalanceAmount, amountDuringTransaction)) {
            return createTransactionOut(initialBalanceAmount, transactionDto, balance);
        }
        return null;
    }

    private TransactionDto createTransactionOut(BigDecimal initialBalanceAmount, TransactionDto transactionDto, BalanceDto balance) {
        BigDecimal newBalanceAmount = initialBalanceAmount.subtract(transactionDto.getAmount());
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
        return transaction.getDirectionCode() == DirectionCode.IN
            && !isNegativeAmount(transaction.getAmount());
    }

    private boolean isNegativeAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isOutTransactionValid(TransactionDto transaction,
                                          BigDecimal initialBalanceAmount,
                                          BigDecimal amountDuringTransaction
    ) {
        return transaction.getDirectionCode() == DirectionCode.OUT
            && isValidAmount(amountDuringTransaction, initialBalanceAmount);
    }

    private boolean isValidAmount(BigDecimal amountDuringTransaction, BigDecimal initialBalanceAmount) {
        if (!(amountDuringTransaction.compareTo(initialBalanceAmount) <= 0)) {
            throw new ApiException("Insufficient Balance");
        }
        return false;
    }

    public List<TransactionDto> getTransactionById(Integer accountId) {
        if (!accountService.isValidAccountId(accountId)) {
            throw new ApiException("AccountId not found");
        }
        return transactionDao.findById(accountId);
    }
}
