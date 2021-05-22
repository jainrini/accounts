package com.mb.module.dao;

import com.mb.module.dto.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionDao {

    void insert(Transaction transaction);

    List<Transaction> findById(Integer id);
}
