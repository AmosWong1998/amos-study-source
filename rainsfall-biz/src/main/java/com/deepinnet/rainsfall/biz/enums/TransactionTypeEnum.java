package com.deepinnet.rainsfall.biz.enums;

import lombok.*;

/**
 * @author amos wong
 * @create 2024/7/1 17:04
 * @Description
 */
@AllArgsConstructor
@Getter
public enum TransactionTypeEnum {

    ROOT("root", "根事务"),

    BRANCH("branch", "子事务");

    private final String code;

    private final String desc;
}
