package com.mb.module.api;

import com.mb.module.config.MessagingConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountApi {

    @Autowired
    private RabbitTemplate template;

    @GetMapping("/details")
    public String getAccountDetails() {
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, "HI");
        return "Hello Account";
    }
}
