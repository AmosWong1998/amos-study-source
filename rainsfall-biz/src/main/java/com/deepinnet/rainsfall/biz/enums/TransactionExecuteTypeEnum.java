package com.deepinnet.rainsfall.biz.enums;

import lombok.*;

/**
 * @author amos wong
 * @create 2024/7/1 17:04
 * @Description
 */
@AllArgsConstructor
@Getter
public enum TransactionExecuteTypeEnum {

    RETRY("retry", "重试"),

    NONE("none", "不需要执行任何操作"),

    ROLLBACK("rollback", "回滚");

    private final String code;

    private final String desc;
}
