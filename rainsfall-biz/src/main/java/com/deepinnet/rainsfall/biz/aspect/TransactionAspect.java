package com.deepinnet.rainsfall.biz.aspect;

import cn.hutool.json.JSONUtil;
import com.deepinnet.rainsfall.biz.annotation.GlobalTransactionStart;
import com.deepinnet.rainsfall.biz.context.*;
import com.deepinnet.rainsfall.biz.dal.dataobject.*;
import com.deepinnet.rainsfall.biz.dal.repository.*;
import com.deepinnet.rainsfall.biz.enums.*;
import com.deepinnet.rainsfall.biz.exception.TransactionException;
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
    private RootTransactionRecordRepository rootTransactionRecordRepository;

    @Resource
    private BranchTransactionRecordRepository branchTransactionRecordRepository;

    @Pointcut(value = "@annotation(com.deepinnet.rainsfall.biz.annotation.GlobalTransactionStart)")
    private void pointcut() {
    }

    @Around("pointcut() && @annotation(transactionStart)")
    private Object around(ProceedingJoinPoint joinPoint, GlobalTransactionStart transactionStart) throws Throwable {
        Object proceedResult = null;

        // 保存分布式事务执行记录
        try {
            if (StringUtils.equals(transactionStart.type(), TransactionTypeEnum.ROOT.getCode())) {
                // 执行业务逻辑
                proceedResult = joinPoint.proceed();

                // 持久化事务
                RootTransactionRecord rootTransactionRecord = buildRootTransactionRecord(joinPoint, TransactionStatusEnum.SUCCESS.getCode());
                rootTransactionRecordRepository.insert(rootTransactionRecord);

                // 初始化事务上下文
                TransactionContext transactionContext = new TransactionContext();
                transactionContext.setRootXid(rootTransactionRecord.getXid());
            } else {
                proceedResult = joinPoint.proceed();

                // 保存分支事务
                BranchTransactionRecord branchTransactionRecord = buildBranchTransactionRecord(joinPoint, transactionStart);
                branchTransactionRecordRepository.insert(branchTransactionRecord);
            }
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                log.error("分布式事务记录保存失败，异常信息为：", e);
            } else {
                if (StringUtils.equals(transactionStart.type(), TransactionTypeEnum.ROOT.getCode())) {
                    // 持久化事务
                    RootTransactionRecord rootTransactionRecord = buildRootTransactionRecord(joinPoint, TransactionStatusEnum.WAIT_EXECUTE.getCode());
                    rootTransactionRecordRepository.insert(rootTransactionRecord);

                    // 初始化事务上下文
                    TransactionContext transactionContext = new TransactionContext();
                    transactionContext.setRootXid(rootTransactionRecord.getXid());
                } else {
                    // 保存分支事务
                    BranchTransactionRecord branchTransactionRecord = buildBranchTransactionRecord(joinPoint, transactionStart);
                    branchTransactionRecordRepository.insert(branchTransactionRecord);
                }
                throw e;
            }
        } finally {
            TransactionContextHolder.removeTransactionContext();
        }

        return proceedResult;
    }


    private RootTransactionRecord buildRootTransactionRecord(ProceedingJoinPoint joinPoint, String status) {
        // 根事务记录
        RootTransactionRecord rootTransactionRecord = new RootTransactionRecord();
        String xid = UUID.randomUUID().toString().replace("-", "");
        rootTransactionRecord.setXid(xid);
        rootTransactionRecord.setType(TransactionTypeEnum.ROOT.getCode());
        rootTransactionRecord.setInterfaceName(joinPoint.getTarget().getClass().getName());
        rootTransactionRecord.setMethodName(joinPoint.getSignature().getName());
        rootTransactionRecord.setParam(JSONUtil.toJsonStr(joinPoint.getArgs()));
        rootTransactionRecord.setStatus(status);
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
