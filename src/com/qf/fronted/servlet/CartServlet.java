package com.qf.fronted.servlet;

import com.qf.base.BaseServlet;
import com.qf.fronted.domain.Goods;
import com.qf.fronted.service.GoodService;
import com.qf.fronted.service.impl.GoodServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 1.主要的作用是当用户到达购物车页面的时候, 要展示商品的信息。
 *   根据商品的id, 查询商品的信息, 商品的id形式为：1001,1002,1003
 */
@WebServlet(value = "/cart", name = "CartServlet")
public class CartServlet extends BaseServlet {

    private GoodService goodService = new GoodServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idsStr = req.getParameter("ids");
        String[] idsArr = idsStr.split(","); //将前端出入过来的ids拆分为数组, 1001,1002,1003

        Integer[] ids = new Integer[idsArr.length]; //生成长度和字符串数组长度一样的整数数组

        for(int i = 0; i < idsArr.length; i++) {
            ids[i] = Integer.parseInt(idsArr[i]);
        }

        List<Goods> goodsList = goodService.getGoodsByIds(ids);

        sendJson(resp, goodsList);
    }
}
