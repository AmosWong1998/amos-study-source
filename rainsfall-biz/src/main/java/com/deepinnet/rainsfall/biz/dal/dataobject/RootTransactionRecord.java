package com.deepinnet.rainsfall.biz.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName root_transaction_record
 */
@TableName(value ="root_transaction_record")
@Data
public class RootTransactionRecord implements Serializable {
    private Long id;

    private String xid;

    private String interfaceName;

    private String methodName;

    private String param;

    private String status;

    private Integer retryCount;

    private static final long serialVersionUID = 1L;
}