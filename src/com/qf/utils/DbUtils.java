package com.qf.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DbUtils {

    private static DruidDataSource druidDataSource;

    static {
        InputStream is =  DbUtils.class.getClassLoader().getResourceAsStream("druid.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
            is.close();
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try{
            conn = druidDataSource.getConnection();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public static void colse(ResultSet rs, Statement statement, Connection conn) {
        try{
            if(null != rs) {
                rs.close();
            }
            if(null != statement) {
                statement.close();
            }
            if(null != conn) {
                conn.close();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
