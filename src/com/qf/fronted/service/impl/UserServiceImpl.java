package com.qf.fronted.service.impl;

import com.qf.admin.domain.User;
import com.qf.fronted.dao.UserDao;
import com.qf.fronted.dao.impl.UserDaoImpl;
import com.qf.fronted.service.UserService;
import com.qf.utils.DbUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private UserDao registerDao = new UserDaoImpl();

    @Override
    public void register(String phone, String username, String password, String email, String sex) {
        registerDao.register(phone, username, password, email, sex);
    }

    @Override
    public boolean checkPhoneOrUsername(String phoneOrUsername) {
        return registerDao.checkPhoneOrUsername(phoneOrUsername);
    }

    @Override
    public User checkUsernameAndPassword(String username, String password) {
        return registerDao.checkUsernameAndPassword(username, password);
    }
}
