package com.mb.module.dao;

import com.mb.module.dto.AccountCreationDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AccountDao {

    void insert(AccountCreationDto dto);

    Optional<AccountCreationDto> findByAccountId(Integer accountId);
}
