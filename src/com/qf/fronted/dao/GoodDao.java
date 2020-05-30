package com.qf.fronted.dao;

import com.qf.fronted.domain.Goods;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GoodDao {

    List<Goods> getAll();

    // 根据指定的id获取对应的商品信息
    List<Goods> getGoodsByIds(Integer[] ids);

    /**
     * 更新库存
     * @param newStockNum   最新库存
     * @param goodId        商品的id
     */
    void updateStockNum(Integer newStockNum, Integer goodId, Connection conn) throws SQLException;

    /**
     * 查询指定商品的库存
     */
    Integer getStockOfSpecifiedGoods(Integer ids);
}
