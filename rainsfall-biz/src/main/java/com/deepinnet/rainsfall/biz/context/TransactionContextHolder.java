package com.deepinnet.rainsfall.biz.context;

import org.springframework.core.NamedThreadLocal;

/**
 * @author amos wong
 * @create 2024/7/1 16:58
 * @Description
 */
public class TransactionContextHolder {

    private static final ThreadLocal<TransactionContext> transactionContextHolder =
            new NamedThreadLocal<>("transactionContext");

    public static void setTransactionContext(TransactionContext transactionContext) {
        transactionContextHolder.set(transactionContext);
    }

    public static TransactionContext getTransactionContext() {
        return transactionContextHolder.get();
    }

    public static String getRootXid() {
        return transactionContextHolder.get().getRootXid();
    }

    public static String getXid() {
        return transactionContextHolder.get().getXid();
    }

    public static void removeTransactionContext() {
        transactionContextHolder.remove();
    }
}
