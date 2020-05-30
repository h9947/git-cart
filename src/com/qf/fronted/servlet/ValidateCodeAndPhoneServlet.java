package com.qf.fronted.servlet;

import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.fronted.domain.SmsCode;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户在注册页面，填写完电话和验证码之后，点击 “下一步” 按钮的触发的。
 */
@WebServlet(value = "/validate", name = "ValidateServlet")
public class ValidateCodeAndPhoneServlet extends BaseServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validateCode = req.getParameter("validateCode");
        String phone = req.getParameter("phone");

        HttpSession session = req.getSession();
        SmsCode smsCode = (SmsCode)session.getAttribute("smsCode");

        Data data = null;

        if(null == smsCode || !smsCode.getCode().equals(validateCode)) { //用户没有验证码
            data = new Data(-2, "验证码错误.");
            sendJson(resp, data);
            return;
        }

        if(!smsCode.isExpire()) { //验证码过期了
            data = new Data(-1, "验证码过期.");
            sendJson(resp, data);
            return;
        }

        // 验证正确，但是手机号不正确, 属于高危风控
        if(smsCode.getCode().equals(validateCode) && !smsCode.getPhone().equals(phone)) {
            data = new Data(-3, "系统有检测到您有恶意注册行为.");
            // TODO 在实际工作中，要将原本的手机推送到风控系统
            sendJson(resp, data);
            return;
        }

        data = new Data(1, "success");
        sendJson(resp, data);
    }
}
