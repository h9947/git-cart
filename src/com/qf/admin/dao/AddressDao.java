package com.qf.admin.dao;

import com.qf.admin.domain.Address;

import java.util.List;

public interface AddressDao {

    List<Address> getAddressByUserId(Integer userId);

    void changeTakeDeliveryAddress(Integer userId, Integer addressId);
}
