package com.qf.fronted.servlet;

import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.fronted.service.UserService;
import com.qf.fronted.service.impl.UserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注册
 */
@WebServlet(value = "/register", name = "RegisterServlet")
public class RegisterServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // username=yuyy&password=1&email=7yty%40qq.com&gender=F&phone=87
        String username = req.getParameter("username");

        // 注册的时候转md5
        String password = DigestUtils.md5Hex(req.getParameter("password"));

        String email = req.getParameter("email");
        String gender = req.getParameter("gender");

        String phone = req.getParameter("phone");

        Data data = null;

        try {
            userService.register(phone, username, password, email, gender);
            data = new Data(1, "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "注册失败");
        }

        sendJson(resp, data);
    }
}
