package com.qf.admin.domain;

import java.util.Date;

/**
 * 前台用户
 */
public class User {
    private Integer id;
    private String name;
    private String email;
    private String sex;
    private Integer status; //激活状态
    private String phone;
    private Date registerDate;

    public User(Integer id, String name, String email, String sex, Integer status, String phone, Date registerDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.status = status;
        this.phone = phone;
        this.registerDate = registerDate;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
