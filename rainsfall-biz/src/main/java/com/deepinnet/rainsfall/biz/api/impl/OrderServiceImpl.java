package com.deepinnet.rainsfall.biz.api.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.deepinnet.rainsfall.biz.annotation.GlobalTransactionStart;
import com.deepinnet.rainsfall.biz.api.*;
import com.deepinnet.rainsfall.biz.constants.TpOrderStatusConstants;
import com.deepinnet.rainsfall.biz.dal.dao.OrderMapper;
import com.deepinnet.rainsfall.biz.dal.dataobject.Order;
import com.deepinnet.rainsfall.biz.dto.*;
import org.apache.dubbo.config.annotation.*;

import javax.annotation.Resource;

/**
 * @author amos wong
 * @create 2024/6/27 17:35
 * @Description
 */
@DubboService
public class OrderServiceImpl implements OrderService {

    @DubboReference
    private UserBalanceService userBalanceService;

    @Resource
    private OrderMapper orderMapper;

    @Override
    @GlobalTransactionStart(type = "root")
    public Long placeOrder(CreateOrderRequest request) {
        // 创建订单
        Long orderNo = this.createOrder(request);

        // 扣减余额
        DeductUserBalanceRequest deductUserBalanceRequest = new DeductUserBalanceRequest();
        deductUserBalanceRequest.setUserNo(request.getUserNo());
        deductUserBalanceRequest.setNeedDeductAmount(request.getPayAmount());
        userBalanceService.deductUserBalance(deductUserBalanceRequest);
        return orderNo;
    }

    @Override
    @GlobalTransactionStart(type = "branch")
    public Long createOrder(CreateOrderRequest createOrderRequest) {
        long orderNo = System.currentTimeMillis();
        Order order = new Order();
        order.setUserNo(createOrderRequest.getUserNo());
        order.setOrderNo(orderNo);
        order.setPayAmount(createOrderRequest.getPayAmount());
        order.setStatus(TpOrderStatusConstants.INIT);
        orderMapper.insert(order);

        return orderNo;
    }

    private void confirmOrder(Long orderNo) {
        Order order = new Order();
        order.setStatus(TpOrderStatusConstants.CREATED);
        orderMapper.update(order, Wrappers.<Order>lambdaUpdate().eq(Order::getOrderNo, orderNo));
    }

    private void cancelOrder(Long orderNo) {
        Order order = new Order();
        order.setStatus(TpOrderStatusConstants.FAIL);
        orderMapper.update(order, Wrappers.<Order>lambdaUpdate().eq(Order::getOrderNo, orderNo));
    }
}
