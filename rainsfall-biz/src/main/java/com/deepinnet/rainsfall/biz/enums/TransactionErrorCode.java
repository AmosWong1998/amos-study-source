package com.deepinnet.rainsfall.biz.enums;

import lombok.*;


/**
 * @author amos wong
 * @create 2024/7/2 15:35
 * @Description
 */
@Getter
@AllArgsConstructor
public enum TransactionErrorCode {
    /**
     * 保存事务信息失败
     */
    SAVE_TRANSACTION_RECORD_ERROR(ErrorLevel.ERROR, ErrorType.SYSTEM, "000", "保存事务信息失败"),

    /**
     * 更新事务信息失败
     */
    UPDATE_TRANSACTION_RECORD_ERROR(ErrorLevel.ERROR, ErrorType.BIZ, "001", "更新事务信息失败");

    /**
     * 错误级别
     */
    private ErrorLevel errorLevel;

    /**
     * 错误类型
     */
    private ErrorType errorType;

    /**
     * 错误编码
     */
    private String code;

    /**
     * 错误描述
     */
    private String desc;

}
