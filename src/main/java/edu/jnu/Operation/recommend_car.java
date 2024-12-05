package edu.jnu.Operation;


import java.sql.*;

public class recommend_car{
    private static final String URL = "jdbc:mysql://localhost:3306/cardb"; // 数据库URL
    private static final String USER = "root";  // 数据库用户名
    private static final String PASSWORD = "root";  // 数据库密码




    //推荐车辆
    public void recommend(int login_id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // 第一步：从用户表中查找该用户喜欢的车型和能源类型
            String userPreferenceSQL = "SELECT car_type, energytype FROM cars_recommendation WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(userPreferenceSQL)) {
                pstmt.setInt(1, login_id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // 获取用户的喜好
                        String carType = rs.getString("car_type");
                        String energyType = rs.getString("energyType");

                        System.out.println("用户喜好的车型: " + carType + ", 能源类型: " + energyType);

                        // 第二步：根据用户的喜好从对应的表中查找符合条件的车辆
                        String carTable = "car_" + carType + "_" + energyType;  // 构建表名

                        // 构建查询 SQL，查找符合条件的车辆
                        String querySQL = "SELECT car_name, min_price, max_price, rating FROM " + carTable
                                + " WHERE min_price < max_price AND max_price > min_price" + " ORDER BY rating DESC";

                        try (PreparedStatement queryStmt = conn.prepareStatement(querySQL);
                             ResultSet carRS = queryStmt.executeQuery()) {

                            // 如果有符合条件的车辆，打印推荐信息
                            while (carRS.next()) {
                                String carName = carRS.getString("car_name");
                                float minPrice = carRS.getFloat("min_price");
                                float maxPrice = carRS.getFloat("max_price");
                                float rating = carRS.getFloat("rating");

                                System.out.println("推荐车型: " + carName + ", 价格区间: " + minPrice + " - " + maxPrice + ", 评分: " + rating);
                            }

                        } catch (SQLException e) {
                            System.out.println("查询车辆数据失败: " + e.getMessage());
                        }
                    } else {
                        System.out.println("未找到该用户的喜好信息！");
                    }
                }
            } catch (SQLException e) {
                System.out.println("查询用户喜好失败: " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    //每次查询之后，Preference_type,preference_price_max,preference_price_min都要更改
    public static void updateCarRecommendation(String string1, String string2, String string3) {
        // SQL 更新语句
        String updateSQL = "UPDATE cars_recommendation " +
                "SET Preference_type = ?, " +
                "preference_price_max = ?, " +
                "preference_price_min = ? " +
                "WHERE <your_condition>"; // 请替换<your_condition>为具体的查询条件

        // 获取数据库连接并执行更新操作
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            // 设置 SQL 语句中的参数
            stmt.setString(1, string1);  // 设置Preference_type
            stmt.setString(2, string2);  // 设置preference_price_max
            stmt.setString(3, string3);  // 设置preference_price_min

            // 执行更新
            int rowsAffected = stmt.executeUpdate();

            // 如果更新成功，则输出提示信息
            if (rowsAffected > 0) {
                System.out.println("当前喜好已更新");
            }
            // 如果更新失败，不显示任何信息
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
