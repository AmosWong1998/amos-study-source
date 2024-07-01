package com.deepinnet.rainsfall.biz.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName branch_transaction_record
 */
@TableName(value ="branch_transaction_record")
@Data
public class BranchTransactionRecord implements Serializable {
    private Long id;

    private String xid;

    private String rootXid;

    private String type;

    private String interfaceName;

    private String methodName;

    private String param;

    private String status;

    private Integer retryCount;

    private static final long serialVersionUID = 1L;
}