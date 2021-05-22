package com.mb.module.api;

import com.mb.module.dto.Account;
import com.mb.module.dto.AccountCreationDto;
import com.mb.module.exceptions.ApiException;
import com.mb.module.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountApi {

    private final AccountService accountService;

    public AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account-create")
    public Account createAccounts(@RequestBody AccountCreationDto dto) {
        return accountService.createAccount(dto);
    }

    @GetMapping("/account")
    public Account getAccountById(@RequestParam Integer accountId) throws ApiException {
        return accountService.getAccountById(accountId);
    }
}
