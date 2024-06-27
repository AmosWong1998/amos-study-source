package com.deepinnet.rainsfall.biz.api;

import com.deepinnet.rainsfall.biz.dto.CreateOrderRequest;

/**
 * @author amos wong
 * @create 2024/6/27 17:35
 * @Description
 */
public interface OrderService {

    Long createOrder(CreateOrderRequest createOrderRequest);
}
