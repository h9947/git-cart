package com.qf.fronted.service;

import java.util.Map;

public interface CartService {

    /**
     * 合并浏览器端与服务端的购物车数据
     * @param browserCartData
     * @param userId
     * @return
     */
    Map<String, Integer> mergeCartData(Map<String, Integer> browserCartData, Integer userId);

    /**
     * 根据用户的id获取用户的购物车信息： {1001: 1, 1002: 3}
     * @param userId
     * @return
     */
    Map<String, Integer> getCartInfoOfUser(Integer userId);

    // 删除对应用户的对应商品信息
    void deleteCartData(Integer userId, Integer goodId);

    /**
     * 在首页中每次添加商品，每次添加一件商品
     * @param userId
     * @param goodId
     */
    void addGood(Integer userId, Integer goodId);


    /**
     * 更新数据库购物车中商品的数量
     * @param userId
     * @param goodId
     * @param num
     */
    void updateGoodNum(Integer userId, Integer goodId, Integer num);
}
