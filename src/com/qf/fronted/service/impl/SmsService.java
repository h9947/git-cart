package com.qf.fronted.service.impl;

// 单纯的做一件事的服务，没必要写接口。
public class SmsService {

    public void sendSms(String code, String phone) {
        // TODO 调用第三方的短信接口。 http://aliyun.com/sms/sendMsg
        System.out.println("往手机号为：" + phone + ", 发送验证码为：" + code);
    }
}
