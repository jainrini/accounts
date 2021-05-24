package com.mb.module.queue;

import com.mb.module.dto.AccountDto;
import com.mb.module.dto.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.mb.module.config.MessagingConfig.ACCOUNT_CREATE_KEY;
import static com.mb.module.config.MessagingConfig.EXCHANGE;
import static com.mb.module.config.MessagingConfig.TRANSACTION_CREATE_KEY;

@Component
@AllArgsConstructor
public class MessageSender {

    private final RabbitTemplate template;

    public void sendAccountCreationMessage(AccountDto account) {
        template.convertAndSend(EXCHANGE, ACCOUNT_CREATE_KEY, account);
    }

    public void sendTransactionCreatedMessage(TransactionDto transactionDto) {
        template.convertAndSend(EXCHANGE, TRANSACTION_CREATE_KEY, transactionDto);
    }
}
