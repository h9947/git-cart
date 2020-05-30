package com.qf.fronted.service.impl;

import com.qf.fronted.dao.CartDao;
import com.qf.fronted.dao.impl.CartDaoImpl;
import com.qf.fronted.service.CartService;
import com.qf.utils.DbUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CartServiceImpl implements CartService {

    private CartDao cartDao = new CartDaoImpl();

    /**
     * 合并浏览器端与服务端的购物车数据
     * @Param browserCartData 是浏览器购物车数据
     * @return
     */
    @Override
    public Map<String, Integer> mergeCartData(Map<String, Integer> browserCartData, Integer userId) {
        // 用户存在于数据库的购物车数据
        Map<String, Integer> serverCartData = cartDao.getCartInfoOfUser(userId);
        /**
         * 数据合并的情况：
         *      1. 数据库没有，浏览器器端有, 以浏览器为准, 表示要新增到数据库。 insert
         *      2. 数据库中有{1001: 3}，浏览器中也有 {1001: 1}, 以浏览器为准: {1001: 1}, 修改数据。update
         *      3. 数据库有，浏览器器没有，数据库的数据最终不变，但是要返回到浏览器中。
         *
         *  针对以上三种情况：将浏览器的数据插入或者更新到数据库，然后查询就行了。
         */

        Set<String> goodIdSet = new HashSet<>();  // 目的是存储数据库中已经存在的购物车商品id, 主要用户更新

        /**
         * 将数据库的数据进行区别：区别浏览器总购物车的数据，哪些是已经存在于数据库的，哪些是不存在的。
         * 例如：数据库中购物车数据：{1003:1, 1002:3, 1012:4, 1011:5}
         *       浏览器中购物车数据：{1001:1, 1002:3, 1012:1, 1013:4}
         *
         * 合并之后数据：{1001:1, 1002:1, 1012:1, 1003:1, 1011:5, 1013:4}
         *
         * 如下的代码找出，数据库和浏览器的商品id的交集。
         */
        serverCartData.forEach((goodId, num) -> {
            if(browserCartData.containsKey(goodId)) {
                goodIdSet.add(goodId);
            }
        });
        /**
         * 因为会有很多的操作，所以要放在一个事务中。
         */
        Connection conn = DbUtils.getConnection();
        try {
            conn.setAutoCommit(false);  //设置事务不自动提交

            Set<String> keySet = browserCartData.keySet();
            for(String goodId : keySet) {
                // 获取对应的商品的数量
                Integer num = browserCartData.get(goodId);
                // 表示交集部分，需要更新数据库的数据
                if(goodIdSet.contains(goodId)) {
                    cartDao.updateShoppingCartData(Integer.parseInt(goodId), num, userId, conn);
                }else { //浏览器中有，但是数据库中没有需要更新
                    cartDao.insertShoppingCartData(Integer.parseInt(goodId), num, userId, conn);
                }
            }
            conn.commit();  // 提交事务
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();   //回滚事务
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            try {
                conn.close();  //关闭连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        serverCartData = cartDao.getCartInfoOfUser(userId);

        return serverCartData;
    }

    @Override
    public Map<String, Integer> getCartInfoOfUser(Integer userId) {
        return cartDao.getCartInfoOfUser(userId);
    }

    @Override
    public void deleteCartData(Integer userId, Integer goodId) {
        cartDao.deleteCartData(userId, goodId);
    }

    @Override
    public void addGood(Integer userId, Integer goodId) {
        // 获取对应用户的对应商品的数量，如果没有返回 null
        Integer num = cartDao.getNumOfSpecifyGood(goodId, userId);
        // 如果数量为null, 表示没有对应的商品
        try {
            if(null == num) {
                cartDao.insertShoppingCartData(goodId, 1, userId, null);
            }else { //如果数量不为null, 表示有对应的商品
                cartDao.updateShoppingCartData(goodId, num + 1, userId, null);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateGoodNum(Integer userId, Integer goodId, Integer num) {
        try {
            cartDao.updateShoppingCartData(goodId, num, userId, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
