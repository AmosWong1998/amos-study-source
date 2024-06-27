package com.deepinnet.rainsfall.biz.api.impl;

import com.deepinnet.rainsfall.biz.api.*;
import com.deepinnet.rainsfall.biz.dal.dao.OrderMapper;
import com.deepinnet.rainsfall.biz.dal.dataobject.Order;
import com.deepinnet.rainsfall.biz.dto.*;
import org.apache.dubbo.config.annotation.*;

/**
 * @author amos wong
 * @create 2024/6/27 17:35
 * @Description
 */
@DubboService
public class OrderServiceImpl implements OrderService {

    @DubboReference
    private UserBalanceService userBalanceService;

    @DubboReference
    private OrderMapper orderMapper;

    @Override
    public Long createOrder(CreateOrderRequest createOrderRequest) {
        long orderNo = System.currentTimeMillis();
        Order order = new Order();
        order.setUserNo(createOrderRequest.getUserNo());
        order.setOrderNo(orderNo);
        order.setPayAmount(createOrderRequest.getPayAmount());
        orderMapper.insert(order);

        // 扣减余额
        DeductUserBalanceRequest deductUserBalanceRequest = new DeductUserBalanceRequest();
        deductUserBalanceRequest.setUserNo(createOrderRequest.getUserNo());
        deductUserBalanceRequest.setNeedDeductAmount(createOrderRequest.getPayAmount());
        userBalanceService.deductUserBalance(deductUserBalanceRequest);
        return orderNo;
    }
}
