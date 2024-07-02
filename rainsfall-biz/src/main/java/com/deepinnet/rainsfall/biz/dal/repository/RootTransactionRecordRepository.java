package com.deepinnet.rainsfall.biz.dal.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepinnet.rainsfall.biz.context.TransactionContextHolder;
import com.deepinnet.rainsfall.biz.dal.dao.RootTransactionRecordMapper;
import com.deepinnet.rainsfall.biz.dal.dataobject.RootTransactionRecord;
import com.deepinnet.rainsfall.biz.enums.*;
import com.deepinnet.rainsfall.biz.exception.TransactionException;
import org.springframework.stereotype.Repository;

/**
 * @author amos wong
 * @create 2024/7/2 09:50
 * @Description
 */
@Repository
public class RootTransactionRecordRepository extends ServiceImpl<RootTransactionRecordMapper, RootTransactionRecord> {

    /**
     * 保存主事务记录
     *
     * @param record
     * @return
     */
    public boolean insert(RootTransactionRecord record) {
        boolean res = this.save(record);
        if (!res) {
            throw new TransactionException(TransactionErrorCode.SAVE_TRANSACTION_RECORD_ERROR);
        }
        return true;
    }

    /**
     * 更新分支事务记录
     *
     * @return
     */
    public boolean update(String xid, String status) {
        // 更新事务为执行成功
        LambdaUpdateWrapper<RootTransactionRecord> wrappers = Wrappers.<RootTransactionRecord>lambdaUpdate()
                .set(RootTransactionRecord::getStatus, TransactionStatusEnum.SUCCESS.getCode())
                .eq(RootTransactionRecord::getXid, TransactionContextHolder.getRootId());
        boolean res = super.update(wrappers);
        if (!res) {
            throw new TransactionException(TransactionErrorCode.UPDATE_TRANSACTION_RECORD_ERROR);
        }

        return true;
    }
}
