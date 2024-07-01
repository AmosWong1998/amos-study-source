package com.deepinnet.rainsfall.biz.dal.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName tcc_transaction_balance
 */
@TableName(value ="balance")
@Data
public class UserBalance implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_no")
    private String userNo;

    @TableField("balance")
    private Long balance;

    @TableField("freeze_balance")
    private Long freezeBalance;
}