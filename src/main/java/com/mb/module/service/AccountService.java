package com.mb.module.service;

import com.mb.module.config.MessagingConfig;
import com.mb.module.dao.AccountDao;
import com.mb.module.dao.BalanceDao;
import com.mb.module.dto.Account;
import com.mb.module.dto.AccountCreationDto;
import com.mb.module.dto.BalanceDto;
import com.mb.module.dto.validator.CurrencyValidator;
import com.mb.module.exceptions.ApiException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;

@Service
public class AccountService {

    private final AccountDao accountDao;
    private final BalanceDao balanceDao;
    private final RabbitTemplate template;

    public AccountService(AccountDao accountDao, BalanceDao balanceDao, RabbitTemplate template) {
        this.accountDao = accountDao;
        this.balanceDao = balanceDao;
        this.template = template;
    }

    public Account createAccount(AccountCreationDto dto) {
        List<String> currencies = dto.getCurrencies();
        validateCurrency(currencies);
        createAccountId(dto);
        createInitialBalancesWithZeroAmount(dto);
        Account allCreatedBalancesForAccount = getAllCreatedBalancesForAccount(dto);
        publishAccountsCreated(allCreatedBalancesForAccount);
        return allCreatedBalancesForAccount;
    }

    private void validateCurrency(List<String> currencies) {
        CurrencyValidator validator = new CurrencyValidator();
        validator.validateListOfCurrency(currencies);
    }

    private Account getAllCreatedBalancesForAccount(AccountCreationDto dto) {
        List<BalanceDto> balances = balanceDao.findByAccountId(dto.getId());
        return Account.builder()
            .id(dto.getId())
            .balanceDtoList(balances)
            .customerId(dto.getCustomerId())
            .build();
    }

    private Integer createAccountId(AccountCreationDto dto) {
        accountDao.insert(dto);
        return dto.getId();
    }

    private void createInitialBalancesWithZeroAmount(AccountCreationDto accountCreationDto) {
        List<String> currencies = accountCreationDto.getCurrencies();
        BalanceDto balanceDto = getBalanceDto(accountCreationDto);
        for (String currency : currencies) {
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

    private void publishAccountsCreated(Account accountIds) {
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, accountIds);
    }

    public Account getAccountById(Integer accountId) throws ApiException {
        if (!isValidAccountId(accountId)) {
            throw new ApiException(format("Account Id %s not found", accountId));
        }
        List<BalanceDto> balances = balanceDao.findByAccountId(accountId);
        Integer customerId = balances.stream().map(v -> v.getCustomerId()).findAny().get();
        return Account.builder()
            .id(accountId)
            .balanceDtoList(balances)
            .customerId(customerId)
            .build();
    }

    public boolean isValidAccountId(Integer accountId) {
        AccountCreationDto account = accountDao.findByAccountId(accountId);
        return account != null;
    }
}
