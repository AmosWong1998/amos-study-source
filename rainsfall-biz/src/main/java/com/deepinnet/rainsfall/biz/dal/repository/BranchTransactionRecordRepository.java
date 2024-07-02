package com.deepinnet.rainsfall.biz.dal.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepinnet.rainsfall.biz.dal.dao.BranchTransactionRecordMapper;
import com.deepinnet.rainsfall.biz.dal.dataobject.BranchTransactionRecord;
import com.deepinnet.rainsfall.biz.enums.TransactionErrorCode;
import com.deepinnet.rainsfall.biz.exception.TransactionException;
import org.springframework.stereotype.Repository;

/**
 * @author amos wong
 * @create 2024/7/2 09:50
 * @Description
 */
@Repository
public class BranchTransactionRecordRepository extends ServiceImpl<BranchTransactionRecordMapper, BranchTransactionRecord> {

    /**
     * 保存事务记录
     *
     * @param record
     * @return
     */
    public boolean insert(BranchTransactionRecord record) {
        boolean res = this.save(record);
        if (!res) {
            throw new TransactionException(TransactionErrorCode.SAVE_TRANSACTION_RECORD_ERROR);
        }
        return true;
    }
}
