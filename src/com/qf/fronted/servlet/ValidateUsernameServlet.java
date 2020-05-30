package com.qf.fronted.servlet;

import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.fronted.service.UserService;
import com.qf.fronted.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户在注册页面，填写完 “用户名” 之后, 验证用户名是否存在。
 */
@WebServlet(value = "/validate-username", name = "ValidateUsernameServlet")
public class ValidateUsernameServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");

        boolean isExists = userService.checkPhoneOrUsername(username);

        Data data = null;
        if(isExists) {  //存在
            data = new Data(-1, "用户名已经存在");
        }else {
            data = new Data(1, "success");
        }

        sendJson(resp, data);
    }
}
