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
import org.springframework.transaction.support.TransactionTemplate;

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
    private SubTransactionRecordRepository subTransactionRecordRepository;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Pointcut(value = "@annotation(com.deepinnet.rainsfall.biz.annotation.GlobalTransactionStart)")
    private void pointcut() {
    }

    @Around("pointcut() && @annotation(transactionStart)")
    private Object around(ProceedingJoinPoint joinPoint, GlobalTransactionStart transactionStart) throws Throwable {
        Object proceedResult = null;

        try {
            // 执行业务逻辑
            proceedResult = joinPoint.proceed();

            // 分布式事务的根事务，首次执行
            if (TransactionContextHolder.getTransactionContext() == null) {
                // 初始化事务上下文
                String rootXid = UUID.randomUUID().toString().replace("-", "");
                TransactionContext transactionContext = new TransactionContext();
                transactionContext.setRootXid(rootXid);
                transactionContext.setXid(rootXid);

                RootTransactionRecord transactionRecord = buildRootTransactionRecord(joinPoint, TransactionStatusEnum.EXECUTING.getCode(), rootXid);
                rootTransactionRecordRepository.insert(transactionRecord);
            }

            // 根事务已经执行完成了，代表整个事务都成功了
            if (StringUtils.equals(TransactionContextHolder.getRootXid(), TransactionContextHolder.getXid())) {
                rootTransactionRecordRepository.update(TransactionContextHolder.getRootXid(), TransactionStatusEnum.EXECUTE_SUCCESS.getCode());
            } else {
                SubTransactionRecord transactionRecord = buildSubTransactionRecord(joinPoint, transactionStart);
                subTransactionRecordRepository.insert(transactionRecord);
            }
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                log.error("分布式事务记录保存失败，异常信息为：", e);
            } else {
                // 业务异常
                transactionTemplate.executeWithoutResult(action -> {
                    rootTransactionRecordRepository.update(TransactionContextHolder.getRootXid(), TransactionStatusEnum.EXECUTE_FAIL.getCode());
                    subTransactionRecordRepository.updateSubTransactionStatusByXid(TransactionContextHolder.getXid(), TransactionStatusEnum.EXECUTE_FAIL.getCode());
                });
                throw e;
            }
        } finally {
            TransactionContextHolder.removeTransactionContext();
        }

        return proceedResult;
    }


    private RootTransactionRecord buildRootTransactionRecord(ProceedingJoinPoint joinPoint, String status, String rootXid) {
        // 根事务记录
        RootTransactionRecord rootTransactionRecord = new RootTransactionRecord();
        rootTransactionRecord.setXid(rootXid);
        rootTransactionRecord.setInterfaceName(joinPoint.getTarget().getClass().getName());
        rootTransactionRecord.setMethodName(joinPoint.getSignature().getName());
        rootTransactionRecord.setParam(JSONUtil.toJsonStr(joinPoint.getArgs()));
        rootTransactionRecord.setStatus(status);
        rootTransactionRecord.setRetryCount(0);
        rootTransactionRecord.setStatus(TransactionStatusEnum.EXECUTING.getCode());
        return rootTransactionRecord;
    }

    private SubTransactionRecord buildSubTransactionRecord(ProceedingJoinPoint joinPoint, GlobalTransactionStart transactionStart) {
        SubTransactionRecord subTransactionRecord = new SubTransactionRecord();
        String xid = UUID.randomUUID().toString().replace("-", "");
        subTransactionRecord.setXid(xid);
        subTransactionRecord.setRootXid(TransactionContextHolder.getRootXid());

        if (StringUtils.isNotBlank(transactionStart.rollbackMethod())) {
            subTransactionRecord.setInterfaceName(transactionStart.rollbackInterface());
            subTransactionRecord.setMethodName(transactionStart.rollbackMethod());
            subTransactionRecord.setParam(transactionStart.rollbackParameter());
        } else {
            subTransactionRecord.setInterfaceName(joinPoint.getTarget().getClass().getName());
            subTransactionRecord.setMethodName(joinPoint.getSignature().getName());
            subTransactionRecord.setParam(JSONUtil.toJsonStr(joinPoint.getArgs()));
        }

        subTransactionRecord.setStatus(TransactionStatusEnum.EXECUTE_SUCCESS.getCode());
        return subTransactionRecord;
    }
}
