package com.qf.fronted.servlet;

import com.qf.admin.domain.Address;
import com.qf.admin.domain.User;
import com.qf.admin.service.AddressService;
import com.qf.admin.service.impl.AddressServiceImpl;
import com.qf.base.BaseServlet;
import com.qf.commons.Data;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet(value = "/addressServlet", name = "FontedAddressServlet")
public class AddressServlet extends BaseServlet {

    private AddressService addressService = new AddressServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("method");

        try {
            Method method = AddressServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改用户的收货地址
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void changeTakeDeliveryAddress (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 需要更改的收货地址
        String addressId = req.getParameter("addressId");

        HttpSession session = req.getSession();

        User user = (User)session.getAttribute("user");
//        Integer id = user.getId();

        Data data =  null;
        try {
            addressService.changeTakeDeliveryAddress(1, Integer.parseInt(addressId));
            data = new Data(1, "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "更改收货地址错误");
        }
        sendJson(resp, data);
    }

    /**
     *  获取当前用户的收货地址
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void getAddressOfUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        User user = (User)session.getAttribute("user");

//        Integer userId = user.getId();  //获取用户id

        // 获取当前用户的收货地址
        List<Address> addresses = addressService.getAddressByUserId(1);

        sendJson(resp, addresses);
    }
}
