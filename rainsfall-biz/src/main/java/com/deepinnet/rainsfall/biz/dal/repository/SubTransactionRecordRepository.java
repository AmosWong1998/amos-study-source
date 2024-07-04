package com.deepinnet.rainsfall.biz.dal.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepinnet.rainsfall.biz.dal.dao.SubTransactionRecordMapper;
import com.deepinnet.rainsfall.biz.dal.dataobject.SubTransactionRecord;
import com.deepinnet.rainsfall.biz.enums.TransactionErrorCode;
import com.deepinnet.rainsfall.biz.exception.TransactionException;
import org.springframework.stereotype.Repository;

/**
 * @author amos wong
 * @create 2024/7/2 09:50
 * @Description
 */
@Repository
public class SubTransactionRecordRepository extends ServiceImpl<SubTransactionRecordMapper, SubTransactionRecord> {

    /**
     * 保存事务记录
     *
     * @param record
     * @return
     */
    public boolean insert(SubTransactionRecord record) {
        boolean res = this.save(record);
        if (!res) {
            throw new TransactionException(TransactionErrorCode.SAVE_TRANSACTION_RECORD_ERROR);
        }
        return true;
    }

    /**
     * 批量更新
     *
     * @return
     */
    public boolean updateSubTransactionStatusByRootXid(String rootXid, String targetStatus) {
        LambdaUpdateWrapper<SubTransactionRecord> wrapper = Wrappers.lambdaUpdate(SubTransactionRecord.class)
                .set(SubTransactionRecord::getStatus, targetStatus)
                .eq(SubTransactionRecord::getRootXid, rootXid);
        boolean res = super.update(wrapper);
        if (!res) {
            throw new TransactionException(TransactionErrorCode.UPDATE_TRANSACTION_RECORD_ERROR);
        }
        return true;
    }

    /**
     * @return
     */
    public boolean updateSubTransactionStatusByXid(String xid, String targetStatus) {
        LambdaUpdateWrapper<SubTransactionRecord> wrapper = Wrappers.lambdaUpdate(SubTransactionRecord.class)
                .set(SubTransactionRecord::getStatus, targetStatus)
                .eq(SubTransactionRecord::getXid, xid);
        boolean res = super.update(wrapper);
        if (!res) {
            throw new TransactionException(TransactionErrorCode.UPDATE_TRANSACTION_RECORD_ERROR);
        }
        return true;
    }
}
