package com.mb.module.service;

import com.mb.module.dao.AccountDao;
import com.mb.module.dto.AccountCreationDto;
import com.mb.module.dto.AccountDto;
import com.mb.module.dto.BalanceDto;
import com.mb.module.enums.TransactionCurrency;
import com.mb.module.exceptions.AccountNotFoundException;
import com.mb.module.exceptions.ApiException;
import com.mb.module.queue.MessageSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;

@Service
@AllArgsConstructor
@Transactional
public class AccountService {

    private final AccountDao accountDao;
    private final BalanceService balanceService;
    private final MessageSender messageSender;

    public AccountDto createAccount(AccountCreationDto accountCreationDto) {
        accountDao.insert(accountCreationDto);

        createInitialBalancesWithZeroAmount(accountCreationDto);

        AccountDto accountWithBalances = getAllCreatedBalancesForAccount(accountCreationDto);
        messageSender.sendAccountCreationMessage(accountWithBalances);
        return accountWithBalances;
    }

    private AccountDto getAllCreatedBalancesForAccount(AccountCreationDto dto) {
        List<BalanceDto> balances = balanceService.findByAccountId(dto.getId());
        return AccountDto.builder()
            .id(dto.getId())
            .balances(balances)
            .customerId(dto.getCustomerId())
            .build();
    }

    private void createInitialBalancesWithZeroAmount(AccountCreationDto accountCreationDto) {
        List<TransactionCurrency> currencies = accountCreationDto.getCurrencies();
        BalanceDto balanceDto = getBalanceDto(accountCreationDto);

        for (TransactionCurrency currency : currencies) {
            balanceDto.setCurrencyCode(currency);
            balanceService.createBalance(balanceDto);
        }
    }

    private BalanceDto getBalanceDto(AccountCreationDto accountCreationDto) {
        return BalanceDto.builder()
            .balanceAmount(BigDecimal.ZERO)
            .accountId(accountCreationDto.getId())
            .countryCode(accountCreationDto.getCountryCode())
            .customerId(accountCreationDto.getCustomerId())
            .build();
    }

    public AccountDto getAccountById(Integer accountId) throws ApiException, AccountNotFoundException {
        AccountCreationDto accounts = getAccountId(accountId);

        List<BalanceDto> balances = balanceService.findByAccountId(accounts.getId());
        Integer customerId = balances.stream().map(v -> v.getCustomerId()).findAny().get();
        return AccountDto.builder()
            .id(accountId)
            .balances(balances)
            .customerId(customerId)
            .build();
    }

    public AccountCreationDto getAccountId(Integer accountId) throws AccountNotFoundException {
        return accountDao.findByAccountId(accountId)
            .orElseThrow(() -> new AccountNotFoundException(
                format("Account id %s not found", accountId)
            ));
    }
}
