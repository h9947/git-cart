package com.qf.fronted.servlet;

import com.qf.admin.domain.User;
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

@WebServlet(value = "/login", name = "LoginServlet")
public class LoginServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.checkUsernameAndPassword(username, DigestUtils.md5Hex(password));

        Data data = null;
        if(null != user) {  //用户名密码正确，放到session中
            req.getSession().setAttribute("user", user);

            data = new Data(1, "success");
        }else { //用户名密码错误
            data = new Data(-1, "用户名或密码错误");
        }
        sendJson(resp, data);
    }
}
