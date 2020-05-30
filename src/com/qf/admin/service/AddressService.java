package com.qf.admin.service;

import com.qf.admin.domain.Address;

import java.util.List;

public interface AddressService {
    // 根据用户id获取用户的收货地址
    List<Address> getAddressByUserId(Integer userId);

    /**
     * 更改指定用户的收货地址，
     * @param userId
     * @param addressId  被设置为收货地址的id
     */
    void changeTakeDeliveryAddress(Integer userId, Integer addressId);
}
