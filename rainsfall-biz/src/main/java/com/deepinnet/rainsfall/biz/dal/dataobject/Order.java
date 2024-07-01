package com.deepinnet.rainsfall.biz.dal.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName tcc_transaction_order
 */
@TableName(value ="order")
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_no")
    private String userNo;

    @TableField("order_no")
    private Long orderNo;

    @TableField("pay_amount")
    private Long payAmount;

    @TableField("status")
    private String status;

}