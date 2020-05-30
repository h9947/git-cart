package com.qf.fronted.servlet;

import com.alibaba.fastjson.JSONObject;
import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.fronted.service.OrderService;
import com.qf.fronted.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// servlet是单实例的
@WebServlet(value = "/order", name = "OrderServlet")
public class OrderServlet extends BaseServlet {

    private OrderService orderService = new OrderServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("method");

        try {
            Method method = OrderServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在结算页面，点击 “确认订单”
     */
    private void ensureOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取商品的id以及商品的数量: {'1001':1, '1002':1}
        String shoppingCartData =  req.getParameter("goodsInfo");
        // 默认的收货地址id
        String takeDelivertyAddressId = req.getParameter("takeDelivertyAddressId");

        // 将json类型的字符串反序列化为 Java对象
        Map<String, Integer> goodsInfo = JSONObject.parseObject(shoppingCartData, Map.class);

        Map<Integer, Integer> goodInfos = new HashMap<>();
        goodsInfo.forEach((k, v) -> {
            goodInfos.put(Integer.parseInt(k), v);
        });

        Data data = orderService.ensureOrder(goodInfos, Integer.parseInt(takeDelivertyAddressId), 18);

        sendJson(resp, data);
    }

    /**
     * 用户在结算页面点击 “确认支付” 的时候调用
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void confirmPay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderNo = req.getParameter("orderNo"); //获取订单编号

        Data data = orderService.confirmPay(orderNo, 18);

        sendJson(resp, data);
    }
}
