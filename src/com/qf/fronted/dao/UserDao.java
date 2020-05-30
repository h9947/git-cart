package com.qf.fronted.dao;

import com.qf.admin.domain.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {

    void register(String phone, String username, String password, String email, String sex);  //手机号码注册

    // 检测手机号是否存在
    boolean checkPhoneOrUsername(String phoneOrUsername);

    // 登录时检测用户名和密码是否存在
    User checkUsernameAndPassword(String username, String password);

    BigDecimal getBalanceOfUser(Integer userId);

    void updateBalanceOfUser(BigDecimal remainBalance, Integer userId, Connection connection) throws SQLException;
}
