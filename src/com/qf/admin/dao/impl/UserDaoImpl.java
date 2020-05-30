package com.qf.admin.dao.impl;

import com.qf.commons.TableData;
import com.qf.admin.dao.UserDao;
import com.qf.admin.domain.User;
import com.qf.utils.DataUtils;
import com.qf.utils.DbUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserDaoImpl implements UserDao {
    /**
     * select u_id id, u_name name, u_email email, u_sex gender, u_status status, u_phone phone, u_register_date registerDate
     *  from t_user where 1 = 1 and u_name like ? and u_sex = ? and u_email like ? and u_status = ? and u_register_date >= ? and u_register_date <= ?
     *   limit ?, ?
     * 查询满足条件的当前页的数据
     */
     @Override
    public TableData<User> getPageData(String name, String sex, Integer status, String email,
                                       Date beginRegisterDate, Date endRegisterDate, Integer beginIndex, Integer pageSize) {
        StringBuffer sql = new StringBuffer("select u_id id, u_name name, u_email email, u_sex sex, u_status status, u_phone phone, u_register_date registerDate ");
        sql.append(" from user where 1 = 1 ");

        Connection conn = DbUtils.getConnection();

         //得到分页的数据
         List<User> users = (List<User>)commonQuery(conn, sql, name, sex, status, email, beginRegisterDate, endRegisterDate, beginIndex,  pageSize, true);

         int total = getTotal(name, sex, status, email, beginRegisterDate, endRegisterDate, conn);

         DbUtils.colse(null, null, conn);  //连接最后关闭

         TableData<User> tableData = new TableData<>(total, users);

         return tableData;
    }

    @Override
    public void activeUser(Integer userId) {
        String sql = "update user set u_status = 1 where u_id = ?";
        Connection conn = DbUtils.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
            ps.executeUpdate();

            DbUtils.colse(null, ps, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询满足条件的总的数据量
    private int getTotal(String name, String sex, Integer status, String email, Date beginRegisterDate, Date endRegisterDate, Connection conn) {
         int total = 0;
         StringBuffer sql = new StringBuffer("select count(*) from user where 1 = 1");

         total = (Integer)commonQuery(conn, sql, name, sex, status, email, beginRegisterDate, endRegisterDate, null,  null, false);
         return total;
    }

    /**
     * 封装通用的查询
     * @param conn                    连接
     * @param sql                     不同sql
     * @param name
     * @param email
     * @param beginRegisterDate
     * @param endRegisterDate
     * @param beginIndex
     * @param pageSize
     * @param isPage                是否为分页，如果为分页传入true, 如果是查总数传入false
     * @return
     */
    private Object commonQuery(Connection conn, StringBuffer sql, String name, String sex, Integer status, String email, Date beginRegisterDate,
                               Date endRegisterDate, Integer beginIndex, Integer pageSize, boolean isPage) {
        Object obj = null;
        // 如果不输入性别， 就不用加条件查询
        if(null != name && !"".equals(name)) {  // "    "
            sql.append(" and u_name like ? ");
        }

        if(!"-1".equals(sex)) {
            sql.append(" and u_sex = ? ");
        }

        if(null != email && !"".equals(email)) {
            sql.append(" and u_email like ? ");
        }

        if(null != status && -1 != status) {
            sql.append(" and u_status = ? ");
        }

        if(null != beginRegisterDate) {
            sql.append(" and u_register_date >= ? ");
        }

        if(null != endRegisterDate) {
            sql.append(" and u_register_date <= ? ");
        }

        if(isPage) {   // 如果为分页，传入true, 就凭借分页的sql
            sql.append(" limit ?, ? ");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());

            int i = 0;
            if(null != name && !"".equals(name)) {  // "    "
                ps.setObject(++i, "%" + name + "%");
            }

            if(!"-1".equals(sex)) {
                ps.setObject(++i, sex);
            }

            if(null != email && !"".equals(email)) {
                ps.setObject(++i, "%" + email + "%");
            }

            if(null != status && -1 != status) {
                ps.setObject(++i, status);
            }

            if(null != beginRegisterDate) {
                ps.setObject(++i, beginRegisterDate);
            }

            if(null != endRegisterDate) {
                ps.setObject(++i, endRegisterDate);
            }

            if(isPage) {
                // int beginIndex = (currentPage - 1) * pageSize;  因为bootstrap-table这个已经将开始索引位置计算好了，所以后端不用重新计算了
                ps.setObject(++i, beginIndex);  //分页开始索引位置
                ps.setObject(++i, pageSize); // 每页多少天数据
            }

            ResultSet rs = ps.executeQuery();

            // 获取分页的数据与获取总数的方式不同。
            if(isPage) {
                obj = DataUtils.getAll(User.class, rs);   // 当前页的数据
            }else {
                rs.next();
                obj = rs.getInt(1);
            }
            DbUtils.colse(rs, ps, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
