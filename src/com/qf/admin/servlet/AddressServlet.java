package com.qf.admin.servlet;

import com.qf.admin.domain.Address;
import com.qf.admin.service.AddressService;
import com.qf.admin.service.impl.AddressServiceImpl;
import com.qf.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/address", name = "AddressServlet")
public class AddressServlet extends BaseServlet {

    private AddressService addressService = new AddressServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");

        List<Address> list = addressService.getAddressByUserId(Integer.parseInt(userId));

        sendJson(resp, list);
    }
}
