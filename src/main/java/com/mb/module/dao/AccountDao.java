package com.mb.module.dao;

import com.mb.module.dto.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountDao {

    public Account find();
}
