package com.deepinnet.rainsfall.biz.exception;

import com.deepinnet.rainsfall.biz.enums.TransactionErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author amos wong
 * @create 2024/7/2 15:34
 * @Description
 */
public class TransactionException extends RuntimeException {

    /**
     * 错误码
     */
    protected TransactionErrorCode errorCode;

    /**
     * 错误时相关参数
     */
    protected Map<String, Object> parameters;

    /**
     * 异常信息
     */
    protected String message;

    /**
     * 异常构造函数。
     *
     * @param errorCode 内部错误码
     */
    public TransactionException(TransactionErrorCode errorCode) {
        this(errorCode, null, null, null);
    }

    /**
     * 异常构造函数。
     *
     * @param errorCode 内部错误码
     * @param cause     错误原因
     */
    public TransactionException(TransactionErrorCode errorCode, Throwable cause) {
        this(errorCode, null, null, cause);
    }

    /**
     * 异常构造函数。
     *
     * <p>内部异常时使用
     *
     * @param errorCode 内部错误码
     * @param message   异常信息
     */
    public TransactionException(TransactionErrorCode errorCode, String message) {
        this(errorCode, message, null, null);
    }


    /**
     * 异常构造函数。
     *
     * <p>内部异常时使用
     *
     * @param errorCode  内部错误码
     * @param message    异常信息
     * @param parameters 错误时相关参数
     */
    public TransactionException(TransactionErrorCode errorCode, String message, Map<String, Object> parameters) {
        this(errorCode, message, parameters, null);
    }

    /**
     * 异常构造函数。
     *
     * @param errorCode 内部错误码
     * @param message   异常信息
     * @param cause
     */
    public TransactionException(TransactionErrorCode errorCode, String message, Throwable cause) {
        this(errorCode, message, null, cause);
    }

    /**
     * 异常构造函数。
     *
     * <p>内部异常时使用
     *
     * @param errorCode
     * @param message
     * @param parameters
     * @param cause
     */
    public TransactionException(TransactionErrorCode errorCode, String message, Map<String, Object> parameters, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.message = message;
        this.parameters = parameters;
    }

    /**
     * @see Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        if (StringUtils.isNotBlank(message)) {
            return message;
        }
        return errorCode.getDesc();
    }

    /**
     * 增加扩展信息
     *
     * @param key
     * @param value
     * @return 自身
     */
    public TransactionException addExtInfo(String key, String value) {
        if (parameters == null) {
            parameters = new HashMap();
        }
        parameters.put(key, value);

        return this;
    }
}
