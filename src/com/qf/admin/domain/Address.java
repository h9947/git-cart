package com.qf.admin.domain;

public class Address {
    private Integer id;
    private String name;
    private String phone;
    private String detail;
    private Integer status;
    private Integer takeDeliveryStatus;

    public Address(Integer id, String name, String phone, String detail, Integer status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.detail = detail;
        this.status = status;
    }

    public Address(Integer id, String name, String phone, String detail, Integer status, Integer takeDeliveryStatus) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.detail = detail;
        this.status = status;
        this.takeDeliveryStatus = takeDeliveryStatus;
    }

    public Address() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTakeDeliveryStatus() {
        return takeDeliveryStatus;
    }

    public void setTakeDeliveryStatus(Integer takeDeliveryStatus) {
        this.takeDeliveryStatus = takeDeliveryStatus;
    }
}
