package com.qf.fronted.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface CartDao {

    // 获取对应用户的购物车数据
    Map<String, Integer> getCartInfoOfUser(Integer userId);

    // 插入
    void insertShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection) throws SQLException;

    // 更新
    void updateShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection) throws SQLException;

    // 删除对应用户的对应商品信息
    void deleteCartData(Integer userId, Integer goodId);

    // 判断用户的购物车中是否存在某个商品
    Integer getNumOfSpecifyGood(Integer goodId, Integer userId);

    //删除对应用户的购物车数据
    void deleteShoppingCartDataOfUser(Integer userId, Connection connection) throws SQLException;

}
