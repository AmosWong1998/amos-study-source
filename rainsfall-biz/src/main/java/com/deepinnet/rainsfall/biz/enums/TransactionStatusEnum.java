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

    INIT("init", "初始化"),

    WAIT_EXECUTE("wait_execute", "待执行"),

    SUCCESS("success", "成功"),

    FAIL("fail", "失败");

    private final String code;

    private final String desc;
}
