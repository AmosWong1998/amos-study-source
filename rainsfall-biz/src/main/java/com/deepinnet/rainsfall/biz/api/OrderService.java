package com.deepinnet.rainsfall.biz.api;

import com.deepinnet.rainsfall.biz.dto.CreateOrderRequest;
import org.mengyun.tcctransaction.api.EnableTcc;

/**
 * @author amos wong
 * @create 2024/6/27 17:35
 * @Description
 */
public interface OrderService {
    /**
     * 下单
     *
     * @param request
     * @return
     */
    Long placeOrder(CreateOrderRequest request);

    /**
     * 创建订单
     *
     * @param createOrderRequest
     * @return
     */
    @EnableTcc
    Long createOrder(CreateOrderRequest createOrderRequest);
}
