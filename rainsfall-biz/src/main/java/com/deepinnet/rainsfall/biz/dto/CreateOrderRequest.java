package com.deepinnet.rainsfall.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author amos wong
 * @create 2024/6/27 17:36
 * @Description
 */
@Data
public class CreateOrderRequest implements Serializable {

    private String userNo;

    private Long payAmount;
}
