package com.mb.module.dao;

import com.mb.module.dto.TransactionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionDao {

    void insert(TransactionDto transaction);

    List<TransactionDto> findById(Integer id);
}
