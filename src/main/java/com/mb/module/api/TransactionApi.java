package com.mb.module.api;

import com.mb.module.dto.TransactionCreationDto;
import com.mb.module.dto.TransactionDto;
import com.mb.module.exceptions.AccountNotFoundException;
import com.mb.module.service.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Transaction API", tags = "Transaction")
@RestController
@RequestMapping("/api")
public class TransactionApi {

    private final TransactionService transactionService;

    public TransactionApi(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public TransactionDto createTransaction(@RequestBody @Valid TransactionCreationDto transaction) throws AccountNotFoundException {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/transactions/{accountId}")
    public List<TransactionDto> getTransaction(@PathVariable Integer accountId) throws AccountNotFoundException {
        return transactionService.getTransactionByAccountId(accountId);
    }
}
