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
 * 用户首页中展示所有商品信息
 */
@WebServlet(value = "/good", name = "GoodServlet")
public class GoodServlet extends BaseServlet {

    private GoodService goodService = new GoodServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Goods> goodsList = goodService.getAll();
        sendJson(resp, goodsList);
    }
}
