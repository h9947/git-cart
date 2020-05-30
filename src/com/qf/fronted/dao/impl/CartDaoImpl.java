package com.qf.fronted.dao.impl;

import com.qf.fronted.dao.CartDao;
import com.qf.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartDaoImpl implements CartDao {

    // 获取对应用户的购物车数据
    @Override
    public Map<String, Integer> getCartInfoOfUser(Integer userId) {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT good_id, num from cart WHERE u_id = ?";

        Connection connection = DbUtils.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                map.put(rs.getString("good_id"), rs.getInt("num"));
            }

            DbUtils.colse(rs, ps, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     *  插入购物车数据
     *
     * @param goodId   商品id
     * @param num      加入购物车的商品数量
     * @param userId   用户id
     */
    public void insertShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection) throws SQLException {
        String insertSql = "insert into cart(good_id, u_id, num, create_time) values(?, ?, ?, ?)";
        Connection conn = connection;
        if(null == conn) {
            conn = DbUtils.getConnection();
        }

        PreparedStatement ps = conn.prepareStatement(insertSql);
        ps.setObject(1, goodId);
        ps.setObject(2, userId);
        ps.setObject(3, num);
        ps.setObject(4, new Date());
        ps.executeUpdate();

        ps.close();
        if(null == connection) {
            conn.close();
        }
    }

    /**
     * 更新数据库中购物车的数据
     * @param goodId  商品id
     * @param num     商品的数量
     * @param userId  用户id
     */
    public void updateShoppingCartData(Integer goodId, Integer num, Integer userId, Connection connection) throws SQLException {
        String updateSql = "update cart set num = ?, update_time = ? where good_id = ? and u_id = ?";

        Connection conn = connection;
        if(null == conn) {
            conn = DbUtils.getConnection();
        }


        PreparedStatement ps = conn.prepareStatement(updateSql);
        ps.setObject(1, num);
        ps.setObject(2, new Date());
        ps.setObject(3, goodId);
        ps.setObject(4, userId);
        ps.executeUpdate();

        ps.close();
        if(null == connection) {
            conn.close();
        }
    }

    @Override
    public void deleteCartData(Integer userId, Integer goodId) {
        String deleteSql = "delete from cart where good_id = ? and u_id = ?";

        Connection conn = DbUtils.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(deleteSql);
            ps.setObject(1, goodId);
            ps.setObject(2, userId);
            ps.executeUpdate();

            DbUtils.colse(null, ps, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getNumOfSpecifyGood(Integer goodId, Integer userId) {
        Integer num = null;
        String sql = "select num from cart where good_id = ? and u_id = ?";

        Connection conn = DbUtils.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, goodId);
            ps.setObject(2, userId);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                num = rs.getInt("num");
            }
            DbUtils.colse(rs, ps, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public void deleteShoppingCartDataOfUser(Integer userId, Connection connection) throws SQLException {
        String deleteSql = "delete from cart where u_id = ?";

        PreparedStatement ps = connection.prepareStatement(deleteSql);
        ps.setObject(1, userId);
        ps.executeUpdate();

        ps.close();
    }
}
