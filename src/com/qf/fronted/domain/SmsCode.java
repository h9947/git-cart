package com.qf.fronted.domain;

import java.time.LocalDateTime;

// 短信验证码
public class SmsCode {
    private String code;  //验证码
    private String phone;
    private LocalDateTime expire; //过期日期

    public SmsCode() {
    }

    // num是过期时间
    public SmsCode(String code, String phone, int num) {
        this.code = code;
        this.phone = phone;
        this.expire = LocalDateTime.now().plusSeconds(num); //过期日期
    }

    // 判断验证码是否过期
    public boolean isExpire() {
        return LocalDateTime.now().isBefore(this.expire);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public void setExpire(LocalDateTime expire) {
        this.expire = expire;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
