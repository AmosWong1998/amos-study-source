package com.deepinnet.rainsfall.controller;

import com.deepinnet.rainsfall.biz.api.OrderService;
import com.deepinnet.rainsfall.biz.dto.CreateOrderRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author amos wong
 * @create 2024/7/1 11:04
 * @Description
 */
@RestController
@RequestMapping("/tcc")
public class TccController {

    @Resource
    private OrderService orderService;

    @PostMapping("/order")
    public Long placeOrder(@RequestBody CreateOrderRequest request) {
        return orderService.placeOrder(request);
    }
}
