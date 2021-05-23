package com.mb.module;

import com.mb.module.config.SwaggerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.mb.module.dao")
@SpringBootApplication
public class AccountsModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsModuleApplication.class, args);
    }

}
