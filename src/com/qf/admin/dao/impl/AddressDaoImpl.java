package com.qf.admin.dao.impl;

import com.qf.admin.dao.AddressDao;
import com.qf.admin.domain.Address;
import com.qf.utils.DataUtils;
import com.qf.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AddressDaoImpl implements AddressDao {

    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        List<Address> list = null;
        String sql = "select a_id id, a_name name, a_phone phone, a_detail detail, a_state status, a_take_delivery_state takeDeliveryStatus from address where u_id = ?";

        Connection conn = DbUtils.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();

            list = DataUtils.getAll(Address.class, rs);

            DbUtils.colse(rs, ps, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void changeTakeDeliveryAddress(Integer userId, Integer addressId) {
        // 将以前的收货地址重置。
        String updateSql = "update address set a_take_delivery_state = 0 where a_take_delivery_state = 1 and u_id = ?";

        //将给定的值该设置为收货地址
        String updateSql2 = "update address set a_take_delivery_state = 1 where a_id = ?";

        Connection conn = DbUtils.getConnection();
        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(updateSql);
            ps.setObject(1, userId);
            ps.executeUpdate();

            ps = conn.prepareStatement(updateSql2);
            ps.setObject(1, addressId);
            ps.executeUpdate();

            DbUtils.colse(null, ps, null);

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
