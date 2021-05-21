package com.mb.module;

import com.mb.module.dto.Account;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MappedTypes(Account.class)
@MapperScan("com.mb.module.dao")
@SpringBootApplication
public class AccountsModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsModuleApplication.class, args);
    }

}
