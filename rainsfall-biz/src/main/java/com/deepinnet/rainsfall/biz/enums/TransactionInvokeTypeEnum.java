package com.deepinnet.rainsfall.biz.enums;

import lombok.*;

/**
 * @author amos wong
 * @create 2024/7/1 17:04
 * @Description
 */
@AllArgsConstructor
@Getter
public enum TransactionInvokeTypeEnum {

    SYSTEM("system", "公司内部系统调用"),

    EXTERNAL_SYSTEM("external_system", "外部系统调用");

    private final String code;

    private final String desc;
}
