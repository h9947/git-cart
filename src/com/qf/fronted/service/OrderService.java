package com.qf.fronted.service;

import com.qf.commons.Data;
import com.qf.fronted.domain.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Data ensureOrder(Map<Integer, Integer> goodsInf, Integer defaultTakeDelivertyAddressId, Integer userId);

    /**
     * 用户确认支付
     * @param orderNo
     * @param userId
     * @return
     */
    Data confirmPay(String orderNo, Integer userId);

    /**
     *
     * @param status
     * @param userId
     * @return
     */
    List<Order> getOrdersByStatus(Integer status, Integer userId);
}
