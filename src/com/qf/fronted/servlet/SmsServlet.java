package com.qf.fronted.servlet;

import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.fronted.domain.SmsCode;
import com.qf.fronted.service.UserService;
import com.qf.fronted.service.impl.SmsService;
import com.qf.fronted.service.impl.UserServiceImpl;
import org.apache.commons.text.RandomStringGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册页面获取手机验证码
 */
@WebServlet(value = "/sms", name = "SmsServlet")
public class SmsServlet extends BaseServlet {

    private SmsService smsService = new SmsService();

    private UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phone = req.getParameter("phone");

        // 检测手机号是否存在
        boolean isExists = userService.checkPhoneOrUsername(phone);

        Data data = null;

        //表示手机已经存在, 后面的内容不用执行
        if(isExists) {
            data = new Data(-2, "手机号已存在");
            sendJson(resp, data);
            return;
        }

        //生成0到9之间的随机数字
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(new char[]{'0', '9'}).build();

        String code = generator.generate(4);  //随机生成长度为4位的数字

        // 短信验证码
        SmsCode smsCode = new SmsCode(code, phone, 120);

        req.getSession().setAttribute("smsCode", smsCode);

        try {
            //调用发送短信的服务
            smsService.sendSms(code, phone);
            data = new Data(1, "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "短信发送失败");
        }
        sendJson(resp, data);
    }
}
