package com.qf.admin.service.impl;

import com.qf.admin.dao.AddressDao;
import com.qf.admin.dao.impl.AddressDaoImpl;
import com.qf.admin.domain.Address;
import com.qf.admin.service.AddressService;
import java.util.List;

public class AddressServiceImpl implements AddressService {

    private AddressDao addressDao = new AddressDaoImpl();

    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        return addressDao.getAddressByUserId(userId);
    }

    /**
     * @param userId
     * @param addressId  被设置为收货地址的id
     */
    @Override
    public void changeTakeDeliveryAddress(Integer userId, Integer addressId) {
        addressDao.changeTakeDeliveryAddress(userId, addressId);
    }
}
