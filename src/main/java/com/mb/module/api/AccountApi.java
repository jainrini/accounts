package com.mb.module.api;

import com.mb.module.dto.AccountCreationDto;
import com.mb.module.dto.AccountDto;
import com.mb.module.exceptions.ApiException;
import com.mb.module.service.AccountService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "Account API", tags = "Account")
@RestController
@RequestMapping("api")
public class AccountApi {

    private final AccountService accountService;

    public AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public AccountDto createAccounts(@RequestBody @Valid AccountCreationDto dto) {
        return accountService.createAccount(dto);
    }

    @GetMapping("/accounts/{accountId}")
    public AccountDto getAccountById(@PathVariable Integer accountId) throws ApiException {
        return accountService.getAccountById(accountId);
    }
}
