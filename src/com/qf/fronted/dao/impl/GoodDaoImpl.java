package com.qf.fronted.dao.impl;

import com.qf.fronted.dao.GoodDao;
import com.qf.fronted.domain.Goods;
import com.qf.utils.DataUtils;
import com.qf.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GoodDaoImpl implements GoodDao {

    @Override
    public List<Goods> getAll() {
        List<Goods> list = null;

        /**
         * SELECT g.id, i.name type, g.price, g.small_pic imageSrc, g.status, g.stock_num stock, g.create_time createTime,
         * 	  g.update_time updateTime, g.title, g.goods_id goodId
         *    from goods g JOIN item i on g.item_id = i.id;
         */
        StringBuffer sql = new StringBuffer("SELECT g.id, i.name type, g.price, g.small_pic imageSrc, g.status, g.stock_num stock, g.create_time createTime, ")
                .append(" g.update_time updateTime, g.title, g.goods_id goodId ")
                .append(" from goods g JOIN item i on g.item_id = i.id ");

        Connection conn = DbUtils.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            list = DataUtils.getAll(Goods.class, rs);

            DbUtils.colse(rs, ps, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 例如 ids = [1001, 1002, 1003]
     *            [1001, 1002, 1003, 1004, 1010]
     *  1001,1002, 1003, 1004, 1010
     */
    @Override
    public List<Goods> getGoodsByIds(Integer[] ids) {
        List<Goods> goodsList = null;

        // select goods_id goodId, title, small_pic imageSrc, price, stock_num stock from goods where goods_id in (?, ?, ?, ?, ?)
        StringBuffer sql = new StringBuffer("select goods_id goodId, title, small_pic imageSrc, price, stock_num stock from goods where goods_id in ( ");

        int length = ids.length;

        // 拼接sql  ->  ?, ?, ?)
        for(int i = 0; i < length; i++){
            if(i == (length - 1)){
                sql.append("?)");
            }else {
                sql.append("?,");
            }
        }

        Connection connection = DbUtils.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            // 设置值
            for(int i = 0; i < length; i++) {
                ps.setObject(i + 1, ids[i]);  // jdbc设置值都是从1开始
            }

            ResultSet rs = ps.executeQuery();
            goodsList = DataUtils.getAll(Goods.class, rs);
            DbUtils.colse(rs, ps, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsList;
    }

    @Override
    public void updateStockNum(Integer newStockNum, Integer goodId, Connection conn) throws SQLException {
        String updateSql = "update goods set stock_num = ? where goods_id = ?";


        PreparedStatement ps = conn.prepareStatement(updateSql);
        ps.setObject(1, newStockNum);
        ps.setObject(2, goodId);

        ps.executeUpdate();

        ps.close();
    }


    @Override
    public Integer getStockOfSpecifiedGoods(Integer goodId) {
        Integer stockNum = 0;
        // select goods_id goodId, title, small_pic imageSrc, price, stock_num stock from goods where goods_id in (?, ?, ?, ?, ?)
        String sql = "select stock_num from goods where goods_id = ? ";

        Connection connection = DbUtils.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            ps.setObject(1, goodId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            stockNum = rs.getInt("stock_num");

            DbUtils.colse(rs, ps, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockNum;
    }
}
