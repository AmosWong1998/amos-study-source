package com.deepinnet.rainsfall.biz.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName sub_transaction_record
 */
@TableName(value ="sub_transaction_record")
@Data
public class SubTransactionRecord implements Serializable {
    private Long id;

    private String xid;

    private String rootXid;

    private String interfaceName;

    private String methodName;

    private String param;

    private String status;

    private static final long serialVersionUID = 1L;
}