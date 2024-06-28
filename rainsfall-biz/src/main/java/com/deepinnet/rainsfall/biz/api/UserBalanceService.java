package com.deepinnet.rainsfall.biz.api;

import com.deepinnet.rainsfall.biz.dto.DeductUserBalanceRequest;
import org.mengyun.tcctransaction.api.EnableTcc;

/**
 * @author amos wong
 * @create 2024/6/27 17:35
 * @Description
 */
public interface UserBalanceService {

    @EnableTcc
    Boolean deductUserBalance(DeductUserBalanceRequest request);
}
