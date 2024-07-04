package com.deepinnet.rainsfall.biz.enums;

import lombok.*;

/**
 * @author amos wong
 * @create 2024/7/1 17:04
 * @Description
 */
@AllArgsConstructor
@Getter
public enum TransactionStatusEnum {

    EXECUTING("executing", "执行中"),

    EXECUTE_FAIL("execute_fail", "执行失败"),

    EXECUTE_SUCCESS("execute_success", "执行成功"),

    RETRYING("retrying", "重试中"),

    RETRYING_SUCCESS("retrying_success", "重试成功"),

    RETRYING_FAIL("retrying_fail", "重试失败"),

    TERMINATED("terminated", "终止");

    private final String code;

    private final String desc;
}
