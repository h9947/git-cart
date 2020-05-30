package com.qf.utils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    /**
     * 返回所有数据
     */
    public static <T> List<T> getAll(Class<T> clazz, ResultSet resultSet) throws Exception {
        List<T> list = new ArrayList<>();

        // while循环是遍历每一行的数据
        while(resultSet.next()) {
            // 生成一个实例， 数据的一行对应着一个对象
            T t = clazz.newInstance();
            // 返回数据库的元数据信息, 元数据信息中包含列明，列的类型， 列的数量
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //获取列的数量
            int colums = resultSetMetaData.getColumnCount();
            // 遍历每一列，取出对应列的数据，然后赋值给对应的属性
            for(int i = 1; i <= colums; i++) {
                //获取字段名，或者别名
                String lable= resultSetMetaData.getColumnLabel(i);

                // 取每列对应的数据
                Object obj = resultSet.getObject(i);

                //获取属性的类型，因为通过反射获取 set方法的时候, 需要使用类型，set方法的参数类型和属性的类型是一致的
                Class<?> filedType = clazz.getDeclaredField(lable).getType();

                /**
                 * 拼接set的方法名: setName
                 * lable.substring(0,1).toUpperCase() 将首字母大写： name -> N
                 */
                String setMethodName = "set" + lable.substring(0,1).toUpperCase() + lable.substring(1);

                // 获取set方法
                Method setMethod = clazz.getMethod(setMethodName, filedType);  // 获取 set方法
                // 给对应的属性设置值
                setMethod.invoke(t, obj);
            }
            list.add(t);
        }
        return list;
    }
}
