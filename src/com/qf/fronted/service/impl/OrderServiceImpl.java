package com.qf.fronted.service.impl;

import com.mysql.cj.xdevapi.DbDoc;
import com.qf.commons.Data;
import com.qf.fronted.dao.CartDao;
import com.qf.fronted.dao.GoodDao;
import com.qf.fronted.dao.OrderDao;
import com.qf.fronted.dao.UserDao;
import com.qf.fronted.dao.impl.CartDaoImpl;
import com.qf.fronted.dao.impl.GoodDaoImpl;
import com.qf.fronted.dao.impl.OrderDaoImpl;
import com.qf.fronted.dao.impl.UserDaoImpl;
import com.qf.fronted.domain.Goods;
import com.qf.fronted.domain.Order;
import com.qf.fronted.service.OrderService;
import com.qf.utils.DbUtils;
import org.apache.commons.text.RandomStringGenerator;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


public class OrderServiceImpl implements OrderService{
    private GoodDao goodDao = new GoodDaoImpl();

    private OrderDao orderDao = new OrderDaoImpl();

    private CartDao cartDao = new CartDaoImpl();

    private UserDao userDao = new UserDaoImpl();

    /**
     * 确认订单
     *
     * @param goodsInfo                     存储的是用户的商品的id以及商品的购买数量
     * @param defaultTakeDelivertyAddressId 收货地址
     */
    @Override
    public Data ensureOrder(Map<Integer, Integer> goodsInfo, Integer defaultTakeDelivertyAddressId, Integer userId) {
        /**
         * 1. 首先要判断库存是否充足，更新库存。
         * 2. 如果库存充足的情况下，生成订单。
         * 3. 插入用户的订单和收货地址
         * 4. 删除购物车数据。
         */
        Set<Integer> goodIds = goodsInfo.keySet(); // 获取所有的商品的id

        // 将集合转换一个对应类型的数组
        Integer[] ids = goodIds.toArray(new Integer[goodIds.size()]);

        List<Goods> goodsList = goodDao.getGoodsByIds(ids);

        Data data = null;
        Connection conn = DbUtils.getConnection();
        try {
            conn.setAutoCommit(false);  //事务要手动提交
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //总的价格， 要在前端支付的价格
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        // 需要返回到前端的数据
        Map<String, Object> resultData = new HashMap<>(); // {"totalPrice": , "orderNo": }

        try {
            for (Integer goodId : goodIds) {
                // 对不同进行上锁
                synchronized (goodId) {
                    // 查询的商品的库存
                    Integer stockNum = goodDao.getStockOfSpecifiedGoods(goodId);

                    // 得到用户需要购买的数量
                    Integer realBuyNum = goodsInfo.get(goodId);

                    // 用户要购买的数量大于实际的库存
                    if (realBuyNum > stockNum) {
                        try {
                            conn.rollback();  //如果某件商品商品库存不够，将之前所有的操作全部回滚掉
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        data = new Data(-1, "库存不够.");
                        return data;
                    } else {  //库存够用户购买
                        // 如果够的话，就更新库存
                        goodDao.updateStockNum(stockNum - realBuyNum, goodId, conn);
                    }
                }
            }

            String oderNo = generateOrderNo(); //获取订单编号
            resultData.put("orderNo", oderNo);

            for (Goods g : goodsList) {
                Integer goodId = g.getGoodId();
                BigDecimal price = g.getPrice(); //商品的价格
                String title = g.getTitle();
                String image = g.getImageSrc();
                Integer num = goodsInfo.get(goodId); //实际购买的商品的数量
                // 插入订单
                orderDao.insertOrder(oderNo, g.getPrice(), num, image, title, 0, 0, userId, goodId, conn);

                /**
                 * add()   加上
                 * multiply()  乘法
                 * divide()   除法
                 * subtract() 减法
                 */
                totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(num)));
            }

            // 插入订单的收货地址
            orderDao.insertOrderAddress(oderNo, defaultTakeDelivertyAddressId, conn);

            // 删除用户的购物车数据
            cartDao.deleteShoppingCartDataOfUser(userId, conn);

            conn.commit();   //提交事务
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            data = new Data(-2, "提交订单失败");
            return data;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        data = new Data(1, "成功");

        resultData.put("totalPrice", totalPrice);

        data.setObj(resultData);  //设置总的价格

        return data;
    }

    // 用户确认支付
    @Override
    public Data confirmPay(String orderNo, Integer userId) {
        /**
         * 确认支付的流程：
         *      1.扣除金额(但是要先判断金额是否够)
         *      2.修改订单的状态，变为 “已支付”
         */
        BigDecimal balance = userDao.getBalanceOfUser(userId); //获取用户的余额

        // 查询对应订单编号的所有的订单，目的是为了计算订单的总金额
        List<Order> list = orderDao.getOrdersOfOrderNo(orderNo);

        // 需要支付的总金额
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        // 集合有流处理
        for(Order o : list) {
            BigDecimal price = o.getGoodPrice(); //获取商品的价格
            Integer num = o.getGoodNum(); //获取商品的数量

            // 单个商品的总价格
            BigDecimal singleGoodsTotalPrice = price.multiply(BigDecimal.valueOf(num));

            totalPrice = totalPrice.add(singleGoodsTotalPrice);
        }

        Data data = null;

        BigDecimal remainBalance = null;
        /**
         * balance如果小于totalPrice，返回 -1
         * balance如果等于totalPrice，返回 0
         * balance如果大于totalPrice，返回 1
         */
        if(balance.compareTo(totalPrice) >= 0) {  // 用户的余额足够支付订单
            remainBalance = balance.subtract(totalPrice);
        }else {  //钱不够
            data = new Data(-1, "你的余额不足.");
            return data;
        }

        Connection connection = DbUtils.getConnection();
        try {
            connection.setAutoCommit(false);  //设置事务不自动提交

            userDao.updateBalanceOfUser(remainBalance, userId, connection);  //更新用户的余额

            orderDao.updateOrderStatus(orderNo, 1, connection);

            connection.commit(); //提交事务
        }catch (Exception ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            data = new Data(-2, "支付失败, 请稍后再试.");
            return data;
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        data = new Data(1, "支付成功");
        return data;
    }

    @Override
    public List<Order> getOrdersByStatus(Integer status, Integer userId) {
        return null;
    }

    /**
     * 生成订单号： 按照 年月日十分秒毫秒 + 6位随机码
     *
     * @return location.href = '订单展示页面.html';
     */
    private String generateOrderNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        String datePrefix = sdf.format(date);

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(new char[]{'0', '9'}).build();

        String randomNum = generator.generate(6);

        return datePrefix + randomNum;
    }
}

