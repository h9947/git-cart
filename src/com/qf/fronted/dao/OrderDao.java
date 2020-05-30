package com.qf.fronted.dao;

import com.qf.fronted.domain.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    void insertOrder(String orderNo, BigDecimal price, Integer num, String goodsImg, String goodsTitle, Integer orderStatus,
                     Integer appraiseStatus, Integer userId, Integer goodId, Connection conn) throws SQLException;

    void insertOrderAddress(String orderNo, Integer defaultTakeDelivertyAddressId, Connection conn) throws SQLException;


    List<Order> getOrdersOfOrderNo(String orderNo);

    /**
     * 根据订单的状态和所属用户查询订单
     * @param status
     * @return
     */
    List<Order> getOrdersByStatus(Integer status, Integer userId);

    /**
     * 更新指定订单的状态
     * @param orderNo        订单编号
     * @param newStatus      最新的状态
     */
    void updateOrderStatus(String orderNo, Integer newStatus, Connection connection) throws SQLException;
}
