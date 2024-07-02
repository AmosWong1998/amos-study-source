package com.deepinnet.rainsfall.biz.aspect;

import cn.hutool.json.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.deepinnet.rainsfall.biz.annotation.GlobalTransactionStart;
import com.deepinnet.rainsfall.biz.context.*;
import com.deepinnet.rainsfall.biz.dal.dao.*;
import com.deepinnet.rainsfall.biz.dal.dataobject.*;
import com.deepinnet.rainsfall.biz.dal.repository.RootTransactionRecordRepository;
import com.deepinnet.rainsfall.biz.enums.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author amos wong
 * @create 2024/7/1 17:20
 * @Description
 */
@Component
@Aspect
@Slf4j
public class TransactionAspect {

    @Resource
    private RootTransactionRecordMapper rootTransactionRecordMapper;

    @Resource
    private RootTransactionRecordRepository rootTransactionRecordRepository;

    @Resource
    private BranchTransactionRecordMapper branchTransactionRecordMapper;

    @Pointcut(value = "@within(com.deepinnet.rainsfall.biz.annotation.GlobalTransactionStart)")
    private void pointcut() {
    }

    @Around("pointcut() && @annotation(transactionStart)")
    private Object around(ProceedingJoinPoint joinPoint, GlobalTransactionStart transactionStart) throws Throwable {
        Object proceedResult = null;

        // 保存分布式事务执行记录
        try {
            if (StringUtils.equals(transactionStart.type(), TransactionTypeEnum.ROOT.getCode())) {
                // 持久化事务
                RootTransactionRecord rootTransactionRecord = buildRootTransactionRecord(joinPoint);
                rootTransactionRecordMapper.insert(rootTransactionRecord);

                // 初始化事务上下文
                TransactionContext transactionContext = new TransactionContext();
                transactionContext.setRootXid(rootTransactionRecord.getXid());
            } else {
                BranchTransactionRecord branchTransactionRecord = buildBranchTransactionRecord(joinPoint, transactionStart);
                branchTransactionRecordMapper.insert(branchTransactionRecord);
            }
        } catch (Exception e) {
            log.error("分布式事务记录保存失败，异常信息为：[0]", e);
        } finally {
            TransactionContextHolder.removeTransactionContext();
        }

        try {
            // 执行业务逻辑
            proceedResult = joinPoint.proceed();

            // 更新事务为执行成功
            LambdaUpdateWrapper<RootTransactionRecord> wrappers = Wrappers.<RootTransactionRecord>lambdaUpdate()
                    .set(RootTransactionRecord::getStatus, TransactionStatusEnum.SUCCESS.getCode())
                    .eq(RootTransactionRecord::getXid, TransactionContextHolder.getRootId());
            rootTransactionRecordRepository.update(wrappers);
        } catch (Exception e) {
            log.error("分布式事务记录保存失败，异常信息为：[0]", e);
        } finally {
            TransactionContextHolder.removeTransactionContext();
        }

        return proceedResult;
    }


    private RootTransactionRecord buildRootTransactionRecord(ProceedingJoinPoint joinPoint) {
        // 根事务记录
        RootTransactionRecord rootTransactionRecord = new RootTransactionRecord();
        String xid = UUID.randomUUID().toString().replace("-", "");
        rootTransactionRecord.setXid(xid);
        rootTransactionRecord.setType(TransactionTypeEnum.ROOT.getCode());
        rootTransactionRecord.setInterfaceName(joinPoint.getTarget().getClass().getName());
        rootTransactionRecord.setMethodName(joinPoint.getSignature().getName());
        rootTransactionRecord.setParam(JSONUtil.toJsonStr(joinPoint.getArgs()));
        rootTransactionRecord.setStatus(TransactionStatusEnum.INIT.getCode());
        rootTransactionRecord.setRetryCount(0);
        return rootTransactionRecord;
    }

    private BranchTransactionRecord buildBranchTransactionRecord(ProceedingJoinPoint joinPoint, GlobalTransactionStart transactionStart) {
        BranchTransactionRecord branchTransactionRecord = new BranchTransactionRecord();
        String xid = UUID.randomUUID().toString().replace("-", "");
        branchTransactionRecord.setXid(xid);
        branchTransactionRecord.setRootXid(TransactionContextHolder.getRootId());
        branchTransactionRecord.setType(TransactionTypeEnum.BRANCH.getCode());

        if (StringUtils.isNotBlank(transactionStart.rollbackMethod())) {
            branchTransactionRecord.setInterfaceName(transactionStart.rollbackInterface());
            branchTransactionRecord.setMethodName(transactionStart.rollbackMethod());
            branchTransactionRecord.setParam(transactionStart.rollbackParameter());
        } else {
            branchTransactionRecord.setInterfaceName(joinPoint.getTarget().getClass().getName());
            branchTransactionRecord.setMethodName(joinPoint.getSignature().getName());
            branchTransactionRecord.setParam(JSONUtil.toJsonStr(joinPoint.getArgs()));
        }

        branchTransactionRecord.setStatus(TransactionStatusEnum.INIT.getCode());
        branchTransactionRecord.setRetryCount(0);
        return branchTransactionRecord;
    }
}
