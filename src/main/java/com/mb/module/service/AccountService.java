package com.mb.module.service;

import com.mb.module.dao.AccountDao;
import com.mb.module.dao.BalanceDao;
import com.mb.module.dto.AccountCreationDto;
import com.mb.module.dto.AccountDto;
import com.mb.module.dto.BalanceDto;
import com.mb.module.enums.TransactionCurrency;
import com.mb.module.exceptions.AccountNotFoundException;
import com.mb.module.exceptions.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.mb.module.config.MessagingConfig.EXCHANGE;
import static com.mb.module.config.MessagingConfig.ROUTING_KEY;
import static java.lang.String.format;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountDao accountDao;
    private final BalanceDao balanceDao;
    private final RabbitTemplate template;

    public AccountDto createAccount(AccountCreationDto accountCreationDto) {
        accountDao.insert(accountCreationDto);

        createInitialBalancesWithZeroAmount(accountCreationDto);

        AccountDto accountWithBalances = getAllCreatedBalancesForAccount(accountCreationDto);
        publishAccountsCreated(accountWithBalances);
        return accountWithBalances;
    }

    private AccountDto getAllCreatedBalancesForAccount(AccountCreationDto dto) {
        List<BalanceDto> balances = balanceDao.findByAccountId(dto.getId());
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
            balanceDao.createBalance(balanceDto);
        }
    }

    private BalanceDto getBalanceDto(AccountCreationDto accountCreationDto) {
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setBalanceAmount(BigDecimal.ZERO);
        balanceDto.setAccountId(accountCreationDto.getId());
        balanceDto.setCustomerId(accountCreationDto.getCustomerId());
        return balanceDto;
    }

    private void publishAccountsCreated(AccountDto account) {
        template.convertAndSend(EXCHANGE, ROUTING_KEY, account);
    }

    public AccountDto getAccountById(Integer accountId) throws ApiException, AccountNotFoundException {
        AccountCreationDto accounts = getAccountId(accountId);

        List<BalanceDto> balances = balanceDao.findByAccountId(accounts.getId());
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
                format("AccountId %s not found", accountId)
            ));
    }
}
