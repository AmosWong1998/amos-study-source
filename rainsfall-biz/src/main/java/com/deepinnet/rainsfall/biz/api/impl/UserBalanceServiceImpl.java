package com.deepinnet.rainsfall.biz.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.deepinnet.rainsfall.biz.api.UserBalanceService;
import com.deepinnet.rainsfall.biz.dal.dao.BalanceMapper;
import com.deepinnet.rainsfall.biz.dal.dataobject.UserBalance;
import com.deepinnet.rainsfall.biz.dto.DeductUserBalanceRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.mengyun.tcctransaction.api.Compensable;

import javax.annotation.Resource;

/**
 * @author amos wong
 * @create 2024/6/27 17:35
 * @Description
 */
@DubboService
public class UserBalanceServiceImpl implements UserBalanceService {

    @Resource
    private BalanceMapper balanceMapper;

    @Override
    @Compensable(confirmMethod = "realDeductUserBalance", cancelMethod = "cancelDeductUserBalance", asyncConfirm = false)
    public Boolean deductUserBalance(DeductUserBalanceRequest request) {
        LambdaQueryWrapper<UserBalance> wrappers = Wrappers.<UserBalance>lambdaQuery()
                .eq(UserBalance::getUserNo, request.getUserNo());
        UserBalance userBalance = balanceMapper.selectOne(wrappers);
        if (userBalance.getBalance() < request.getNeedDeductAmount()) {
            throw new RuntimeException("余额不足");
        }

        UserBalance tpOrderBalanceDO = new UserBalance();
        tpOrderBalanceDO.setFreezeBalance(request.getNeedDeductAmount());
        int res = balanceMapper.update(tpOrderBalanceDO, Wrappers.<UserBalance>lambdaUpdate().eq(UserBalance::getUserNo, request.getUserNo()));
        return res == 1;
    }

    private void realDeductUserBalance(DeductUserBalanceRequest request) {
        LambdaQueryWrapper<UserBalance> wrappers = Wrappers.<UserBalance>lambdaQuery()
                .eq(UserBalance::getUserNo, request.getUserNo());
        UserBalance userBalance = balanceMapper.selectOne(wrappers);
        if (userBalance.getBalance() < request.getNeedDeductAmount()) {
            throw new RuntimeException("余额不足");
        }

        UserBalance tpOrderBalanceDO = new UserBalance();
        tpOrderBalanceDO.setBalance(userBalance.getBalance() - request.getNeedDeductAmount());
        tpOrderBalanceDO.setFreezeBalance(0L);
        balanceMapper.update(tpOrderBalanceDO, Wrappers.<UserBalance>lambdaUpdate().eq(UserBalance::getUserNo, request.getUserNo()));
    }

    private void cancelDeductUserBalance(DeductUserBalanceRequest request) {
        UserBalance tpOrderBalanceDO = new UserBalance();
        tpOrderBalanceDO.setFreezeBalance(0L);
        balanceMapper.update(tpOrderBalanceDO, Wrappers.<UserBalance>lambdaUpdate().eq(UserBalance::getUserNo, request.getUserNo()));
    }
}