package com.qf.admin.service;

import com.qf.commons.TableData;
import com.qf.admin.domain.User;

import java.util.Date;

public interface UserService  {

    /**
     * 根据用户名、性别、邮箱、注册时期来查询分页的数据
     * @param name
     * @param gender
     * @param email
     * @param beginRegisterDate
     * @param endRegisterDate
     * @param beginIndex           当前页
     * @param pageSize              每页多条数据
     * @return
     */
    TableData<User> getPageData(String name, String gender, Integer status, String email,
                                Date beginRegisterDate, Date endRegisterDate, Integer beginIndex, Integer pageSize);

    // 激活用户
    void activeUser(Integer userId);
}
