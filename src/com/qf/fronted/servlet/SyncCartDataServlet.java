package com.qf.fronted.servlet;

import com.alibaba.fastjson.JSONObject;
import com.qf.admin.domain.User;
import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.fronted.service.CartService;
import com.qf.fronted.service.impl.CartServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 同步购物车数据：
 *  1. 用户登录成功之后，同步购物车数据
 *  2. 在购物车页面删除购物车数据。
 *  3. 在首页中添加商品到购物车。
 *  4. 在购物车页面，"+" "-" 商品。
 */
@WebServlet(value = "/sync-cart", name = "SyncCartDataServlet")
public class SyncCartDataServlet extends BaseServlet {

    private CartService cartService = new CartServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("method");

        try {
            Method method = SyncCartDataServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 添加商品到购物车, 首页中点击添加按钮
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void addGood(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String goodId = req.getParameter("goodId");

         HttpSession session = req.getSession();
         User user = (User)session.getAttribute("user");
        Integer userId = user.getId();

        Data data = null;
        try{
            cartService.addGood(userId, Integer.parseInt(goodId));
            data = new Data(1, "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "添加到购物车失败");
        }
        sendJson(resp, data);
    }

    /**
     * 在购物车页面，当用户点击 ‘+’ 和 ‘-’ 的时候触发的，就是用户要去改变商品的数量
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void changeGoodNum(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String goodId = req.getParameter("goodId");
        String num = req.getParameter("num");

         HttpSession session = req.getSession();
         User user = (User)session.getAttribute("user");
         Integer userId = user.getId();

        Data data = null;
        try{
            cartService.updateGoodNum(userId, Integer.parseInt(goodId), Integer.parseInt(num));
            data = new Data(1, "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "更新购物车失败");
        }
        sendJson(resp, data);
    }

    /**
     * 该方法在购物车页面，用户点击删除的时候触发。
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void removeGood(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String goodId = req.getParameter("goodId");

         HttpSession session = req.getSession();
         User user = (User)session.getAttribute("user");
         Integer userId = user.getId();

        cartService.deleteCartData(userId, Integer.parseInt(goodId));
    }

    /**
     * 用户登录成功之后，需要将前端的购物车数据与服务器端合并：
     *    1.前端购物车没有数据。
     *    2.前端购物车有数据。 求并集，并以浏览器为准 ： {1， 2， 3}   {2, 5, 6}  -> {1, 2, 3, 5, 6}
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void getCartInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取浏览器端的购物车数据
        String browserShoppingCartData = req.getParameter("cartData");

        HttpSession session = req.getSession();

        //获取用户信息，因为登录成功之后我们已经在session中设置了用户的信息
         User user = (User)session.getAttribute("user");

         Integer userId = user.getId(); //获取用户的id

        if(null != browserShoppingCartData && !"".equals(browserShoppingCartData)) {
            // 表示前端购物车有数据，那么就需要与服务端对应的购物车进行合并
            // browserShoppingCartData: {1001: 3, 1002: 1, 1013: 2}, JSONObject.parseObject是将json
            // 字符串转换为java对象
            Map<String, Integer> cartData = JSONObject.parseObject(browserShoppingCartData, Map.class);

            Map<String, Integer> cartInfo = cartService.mergeCartData(cartData, userId);

            Data data = new Data(1, "购物车中有数据");
            data.setObj(cartInfo);
            sendJson(resp, data);
        }else {
            // 前端购物车没有数据，那么查询数据库中对应的购物车中是否有数据，然后返回

            // 获取用用户购物车数据如何封装： {1001: 1, 1002: 3, 1013: 5}
            Map<String, Integer> map = cartService.getCartInfoOfUser(userId);

            /**
             * 在Dao第一行代码，我们直接 new HashMap, 如果用户购物车中没有数据,
             * 返回到前端的json数据：{}, 就会导致前端不好判断
             */
            Data data = null;
            if(map.size() == 0) {  //用户的购物车中没有数据
                data = new Data(-2, "购物车中没有数据");
            }else {
                data = new Data(1, "购物车中有数据");
                data.setObj(map);  // {code: 1, msg:'', obj: {}}
            }
            sendJson(resp, data);
        }
    }
}
