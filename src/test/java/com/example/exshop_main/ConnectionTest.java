package com.example.exshop_main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionTest {
    
    public static Connection getConnection() {
        // 定义连接
        Connection connection = null;
        
        try {
            // 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exshop", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public static List<HashMap<String, Object>> getMysqlData() {
        Connection connection = null;
        // 预执行加载
        PreparedStatement preparedStatement = null;
        // 结果集
        ResultSet resultSet = null;
        
        connection = getConnection();
        
        String sqlString = "select * from shop_user";
        
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
        
        try {
            preparedStatement = connection.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();
            HashMap<String, Object> map = null;
            while (resultSet.next()) {
                map = new HashMap<String, Object>();
                map.put("name", resultSet.getString("username"));
                list.add(map);
            }
         } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static void main(String[] args) {
        List<HashMap<String, Object>> mysqlData = getMysqlData();
        for(HashMap<String, Object> map : mysqlData) {
            System.out.println(map.get("name"));
        }
    }
}