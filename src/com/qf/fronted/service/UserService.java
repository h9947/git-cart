package com.qf.fronted.service;

import com.qf.admin.domain.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserService {

    void register(String phone, String username, String password, String email, String sex);  //手机号码注册

    // 检测手机号或者用户名是否存在
    boolean checkPhoneOrUsername(String phoneOrUsername);

    // 登录时候验证用户名和密码
    User checkUsernameAndPassword(String username, String password);
}
