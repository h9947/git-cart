package com.qf.admin.servlet;

import com.qf.commons.TableData;
import com.qf.admin.domain.User;
import com.qf.admin.service.UserService;
import com.qf.admin.service.impl.UserServiceImpl;
import com.qf.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 1.对于前端用户来说, 后端不能提供删除、添加、编辑功能。
 */
@WebServlet(value = "/user", name = "UserServlet")
public class UserServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("method");
        try {
            Method method = UserServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserPageData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // http://localhost:8081/my_cart_war_exploded/user?order=asc&offset=0&limit=10&name=&gender=-1&status=-1&email=&begin=&end=&method=getUserPageData
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");

        String stat = req.getParameter("status");
        String begin = req.getParameter("begin");
        String end = req.getParameter("end");

        String offset = req.getParameter("offset");
        String limit = req.getParameter("limit");

        Integer status = Integer.parseInt(stat);  // 将状态值转为整数，根本原因在于数据库是整数
        // beginRegisterDate, endRegisterDate
        Date beginRegisterDate = null;
        Date endRegisterDate = null;

        if(null != begin && !"".equals(begin.trim())) {
            beginRegisterDate = dateFormat(begin, "yyyy-MM-dd HH:mm:ss");
        }

        if(null != end && !"".equals(end.trim())) {
            endRegisterDate = dateFormat(end, "yyyy-MM-dd HH:mm:ss");
        }

        /**
         * http://localhost:8081/my_cart_war_exploded/user?order=asc&offset=0&limit=10
         * limit 是分页的数据
         * offset开始索引位置。
         */
        TableData<User> tableData = userService.getPageData(name, gender, status, email,
                beginRegisterDate, endRegisterDate, Integer.parseInt(offset), Integer.parseInt(limit));
        sendJson(resp, tableData);
    }

    // 激活用户
    private void activeUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");

        Map<String, Object> map = new HashMap<>();
        try {
            userService.activeUser(Integer.parseInt(userId));
            map.put("code", 1);
            map.put("msg", "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            map.put("code", -1);
            map.put("msg", "激活失败");
        }

        sendJson(resp, map); // {code: 1, msg: ''}
    }

    /**
     * 将日期类型的字符串转换为 Date类型
     * @param dateStr   日期类型的字符串
     * @param pattern   模式   yyyy-MM-dd
     * @return
     */
    private Date dateFormat(String dateStr, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
