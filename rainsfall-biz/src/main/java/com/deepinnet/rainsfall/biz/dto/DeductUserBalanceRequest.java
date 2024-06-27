package com.deepinnet.rainsfall.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author amos wong
 * @create 2024/6/27 17:37
 * @Description
 */
@Data
public class DeductUserBalanceRequest implements Serializable {

    private String userNo;

    private Long needDeductAmount;
}
