package com.mb.module.service;

import com.mb.module.dao.TransactionDao;
import com.mb.module.dto.BalanceDto;
import com.mb.module.dto.Transaction;
import com.mb.module.enums.DirectionCode;
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

    public Transaction createTransaction(Transaction transaction) {
        Integer accountId = transaction.getAccountId();
        if (!accountService.isValidAccountId(accountId)) {
            throw new ApiException("AccountId not found");
        }
        BalanceDto balance = balanceService.getByAccountIdAndCurrency(
            accountId,
            transaction.getCurrencyCode()
        );
        BigDecimal newBalanceAmount = null;
        BigDecimal initialBalanceAmount = balance.getBalanceAmount();
        BigDecimal amountDuringTransaction = transaction.getAmount();

        if (isTransactionValid(transaction)) {
            newBalanceAmount = initialBalanceAmount.add(amountDuringTransaction);
        } else if (isOutTransactionValid(transaction, initialBalanceAmount, amountDuringTransaction)) {
            newBalanceAmount = initialBalanceAmount.subtract(amountDuringTransaction);
        }
        if (newBalanceAmount != null) {
            balanceService.updateBalanceAmount(newBalanceAmount, balance.getId());
            transaction.setInitialBalanceAmount(initialBalanceAmount);
            transaction.setBalanceAfterTransaction(newBalanceAmount);
            transactionDao.insert(transaction);
            return transaction;
        }
        return null;
    }

    private boolean isTransactionValid(Transaction transaction) {
        return transaction.getDirectionCode() == DirectionCode.IN &&
            !isNegativeAmount(transaction.getAmount());
    }

    private boolean isNegativeAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isOutTransactionValid(Transaction transaction,
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

    public List<Transaction> getTransactionById(Integer accountId) {
        return transactionDao.findById(accountId);
    }
}
