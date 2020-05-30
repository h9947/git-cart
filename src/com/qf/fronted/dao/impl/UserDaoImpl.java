package com.qf.fronted.dao.impl;

import com.qf.admin.domain.User;
import com.qf.fronted.dao.UserDao;
import com.qf.utils.DbUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserDaoImpl implements UserDao {

    @Override
    public void register(String phone, String username, String password, String email, String sex) {
        String sql = "insert into user (u_name, u_email, u_password, u_sex, u_status, u_phone, u_register_date) values(?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DbUtils.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, username);
            ps.setObject(2, email);
            ps.setObject(3, password);
            ps.setObject(4, sex);
            ps.setObject(5, 1);
            ps.setObject(6, phone);
            ps.setObject(7, new Date());

            ps.executeUpdate();
            DbUtils.colse(null, ps, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 检测手机号是否存在
    @Override
    public boolean checkPhoneOrUsername(String phoneOrUsername) {
        boolean isExists = false;
        String sql = "select 1 from user where  u_phone = ? or u_name = ? ";
        Connection connection = DbUtils.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setObject(1, phoneOrUsername);
            ps.setObject(2, phoneOrUsername);

            ResultSet rs = ps.executeQuery();

            isExists = rs.next();

            DbUtils.colse(rs, ps, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isExists;
    }

    // 检测用户名和密码是否正确
    @Override
    public User checkUsernameAndPassword(String username, String password) {
        User user = null;

        StringBuffer sql = new StringBuffer("select u_id id, u_name name, u_email email, u_sex sex, ")
                .append(" u_status status, u_phone phone, u_register_date registerDate ")
                .append(" from user ")
                .append(" where u_name = ? and u_password = ?");

        Connection conn = DbUtils.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setObject(1, username);
            ps.setObject(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                // (Integer id, String name, String email, String sex, Integer status, String phone, Date registerDate) {
                user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("sex"),
                        rs.getInt("status"), rs.getString("phone"), rs.getDate("registerDate"));
            }

            DbUtils.colse(rs, ps, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // 获取用户的余额
    @Override
    public BigDecimal getBalanceOfUser(Integer userId) {
        BigDecimal bigDecimal = null;
        String sql = "select u_balance from user where u_id = ?";
        Connection connection = DbUtils.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();

            bigDecimal = rs.getBigDecimal("u_balance");
            DbUtils.colse(rs, ps, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bigDecimal;
    }

    // 更新用户余额
    @Override
    public void updateBalanceOfUser(BigDecimal remainBalance, Integer userId, Connection connection) throws SQLException {
        String updateSql = "update user set u_balance = ? where u_id = ? ";

        PreparedStatement ps = connection.prepareStatement(updateSql);

        ps.setObject(1, remainBalance);
        ps.setObject(2, userId);
        ps.executeUpdate();

        ps.close();
    }
}
