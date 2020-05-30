package com.qf.admin.dao;

import com.qf.commons.TableData;
import com.qf.admin.domain.User;

import java.util.Date;

public interface UserDao {
    TableData<User> getPageData(String name, String gender, Integer status, String email,
                                Date beginRegisterDate, Date endRegisterDate, Integer beginIndex, Integer pageSize);

    void activeUser(Integer userId);
}
