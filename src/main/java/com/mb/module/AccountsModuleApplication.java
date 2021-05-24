package com.mb.module;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.mb.module.dao")
@EnableTransactionManagement
@SpringBootApplication
public class AccountsModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsModuleApplication.class, args);
    }

}
