package com.qf.admin.service.impl;

import com.qf.commons.TableData;
import com.qf.admin.dao.UserDao;
import com.qf.admin.dao.impl.UserDaoImpl;
import com.qf.admin.domain.User;
import com.qf.admin.service.UserService;

import java.util.Date;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public TableData<User> getPageData(String name, String gender, Integer status, String email, Date beginRegisterDate,
                                       Date endRegisterDate, Integer beginIndex, Integer pageSize) {
        return userDao.getPageData(name, gender, status, email, beginRegisterDate, endRegisterDate, beginIndex, pageSize);
    }

    /**
     * 激活指定的用户
     * @param userId
     */
    @Override
    public void activeUser(Integer userId) {
        userDao.activeUser(userId);
    }
}
