package edu.jnu.Operation;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class salerating {

    private static final String URL = "jdbc:mysql://localhost:3306/cardb"; // 数据库URL
    private static final String USER = "root";  // 数据库用户名
    private static final String PASSWORD = "root";  // 数据库密码



    public void salesRating() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 所有可能的车型类型
            String[] carTypes = {"sedan", "mpv", "suv"};
            // 所有可能的能源类型
            String[] energyTypes = {"oil", "electric"};

            // 创建一个容器，保存所有查询结果
            List<String> allResults = new ArrayList<>();

            // 构建查询每个表的 SQL 语句
            for (String carType : carTypes) {
                for (String energyType : energyTypes) {
                    String tableName = "car_" + carType + "_" + energyType;  // 动态构建表名

                    // 构建查询 SQL：按 RATING 降序排列，限制结果为前 15
                    String sql = "SELECT * FROM " + tableName + " ORDER BY rating DESC LIMIT 15";

                    // 执行查询
                    try (PreparedStatement pstmt = conn.prepareStatement(sql);
                         ResultSet rs = pstmt.executeQuery()) {

                        // 处理查询结果
                        while (rs.next()) {
                            // 获取并打印该表的所有属性
                            String carName = rs.getString("car_name");
                            float minPrice = rs.getFloat("min_price");
                            float maxPrice = rs.getFloat("max_price");
                            float rating = rs.getFloat("rating");


                            // 将结果拼接成字符串并加入容器
                            String result = "车型: " + carName + ", 价格区间: " + minPrice + " - " + maxPrice +
                                    ", 评分: " + rating + ", 能源类型: " + energyType +
                                    ", 车型类型: " + carType;
                            allResults.add(result);
                        }
                    }
                }
            }

            // 输出所有查询结果
            if (allResults.isEmpty()) {
                System.out.println("没有符合条件的推荐车辆！");
            } else {
                for (String result : allResults) {
                    System.out.println(result);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
