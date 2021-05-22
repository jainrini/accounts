package com.mb.module.dao;

import com.mb.module.dto.AccountCreationDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountDao {

    void insert(AccountCreationDto dto);

    AccountCreationDto findByAccountId(Integer accountId);
}
