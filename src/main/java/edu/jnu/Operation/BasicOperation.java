package edu.jnu.Operation;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicOperation {

    private static final String URL = "jdbc:mysql://localhost:3306/cardb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";




    private Connection getConnection() throws SQLException {
        // 连接数据库的代码（你可以根据你的数据库连接设置修改）
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/cardb", "root", "root");
    }


    public void updateCar(int carid, String carName, Double minPrice, Double maxPrice, Double rating, String image, String type, String energyType, String newtype, String newenergyType) {
        // 构建表名
        String tableName = "car_" + type + "_" + energyType;

        // 检查是否是合法的类型和能源类型
        if (!isValidType(type) || !isValidEnergyType(energyType)) {
            System.out.println("无效的类型或能源类型！");
            return;
        }

        // 1. 获取当前的 image 值
        String currentImage = null;
        String selectSQL = "SELECT image FROM " + tableName + " WHERE Carid = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, carid);  // 设置 Carid 条件
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    currentImage = rs.getString("image"); // 获取原来的 image 值
                    System.out.println("当前的 image 值是: " + currentImage); // 打印 currentImage 以调试
                } else {
                    System.out.println("未找到对应的 Carid: " + carid);
                    return;  // 如果没有找到记录，则不执行后续操作
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // 2. 构建动态的 UPDATE SQL 语句
        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + tableName + " SET ");
        boolean first = true;

        if (carName != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("Car_name = ?");
            first = false;
        }
        if (minPrice != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("Min_price = ?");
            first = false;
        }
        if (maxPrice != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("Max_price = ?");
            first = false;
        }
        if (rating != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("rating = ?");
            first = false;
        }
        if (image != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("image = ?");
            first = false;
        } else {
            if (!first) sqlBuilder.append(", ");
            // 如果 image 为 null，直接保留原来的 image（currentImage）
            sqlBuilder.append("image = ?");
            first = false;
        }

        // 添加 WHERE 条件
        sqlBuilder.append(" WHERE Carid = ?");
        String sql = sqlBuilder.toString();
        System.out.println("Generated SQL: " + sql); // 打印生成的 SQL，方便调试

        // 执行更新操作
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            if (carName != null) {
                pstmt.setString(paramIndex++, carName);
            }
            if (minPrice != null) {
                pstmt.setDouble(paramIndex++, minPrice);
            }
            if (maxPrice != null) {
                pstmt.setDouble(paramIndex++, maxPrice);
            }
            if (rating != null) {
                pstmt.setDouble(paramIndex++, rating);
            }
            // 如果传入的 image 是 null，则使用 currentImage（从查询中获得的值）
            if (image != null) {
                pstmt.setString(paramIndex++, image);
            } else {
                pstmt.setString(paramIndex++, currentImage);  // 使用原来的 image
            }
            pstmt.setInt(paramIndex, carid); // 最后设置 Carid 条件

            // 执行更新操作
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("更新成功，影响了 " + rowsAffected + " 行。");

            // 3. 插入更新后的元组到新的表
            insertCar(newtype, newenergyType, carName, minPrice, maxPrice, rating, currentImage);

            // 4. 删除原记录
            deleteCar(carid, type, energyType);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // 插入新记录的方法
    public void insertCar(String type, String energyType, String carName, double minPrice, double maxPrice, double rating, String image) {
        // 构建目标表名
        String tableName = "car_" + type + "_" + energyType;

        // 1. 先检查是否已经存在相同的 car_name
        String checkSQL = "SELECT COUNT(*) FROM " + tableName + " WHERE Car_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {

            checkStmt.setString(1, carName); // 设置 car_name 参数
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // 如果已经存在该 car_name
                    System.out.println("该 Car_name 已存在，插入操作被跳过！");
                    return;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // 2. 执行插入操作，不需要传入 carId，数据库会自动生成
        String sql = "INSERT INTO " + tableName + " (Car_name, Min_price, Max_price, rating, image) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carName);
            pstmt.setDouble(2, minPrice);
            pstmt.setDouble(3, maxPrice);
            pstmt.setDouble(4, rating);
            pstmt.setString(5, (image != null ? image : null));  // 如果没有提供 image，则插入 NULL

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("插入成功，影响了 " + rowsAffected + " 行。");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    // 删除旧记录的方法
    public void deleteCar(int carId, String type, String energyType) {
        String tableName = "car_" + type + "_" + energyType;
        String sql = "DELETE FROM " + tableName + " WHERE Carid = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("删除成功，影响了 " + rowsAffected + " 行。");
            } else {
                System.out.println("未找到对应的记录！");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Do selectCarById(int carId, String type, String energyType) {
        // 构建表名
        String tableName = "car_" + type + "_" + energyType;

        // 检查是否是合法的类型和能源类型
        if (!isValidType(type) || !isValidEnergyType(energyType)) {
            System.out.println("无效的类型或能源类型！");
            return null;
        }

        // 查询 SQL（去除 EnergyType 字段）
        String sql = "SELECT Carid, Car_name, Min_price, Max_price, rating, image FROM " + tableName + " WHERE Carid = ?";
        System.out.println( sql);  // 打印生成的 SQL，确保正确

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数
            pstmt.setInt(1, carId);  // 设置 Carid（条件）

            // 执行查询操作
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 获取查询结果
                    int id = rs.getInt("Carid");
                    String carName = rs.getString("Car_name");
                    double minPrice = rs.getDouble("Min_price");
                    double maxPrice = rs.getDouble("Max_price");
                    float rating = rs.getFloat("rating");
                    String image = rs.getString("image");
                    Do d =new Do(carName, minPrice, maxPrice, rating, image);
                    // 打印查询结果
                    System.out.println("CarId: " + id);
                    System.out.println("Car Name: " + carName);
                    System.out.println("Min Price: " + minPrice);
                    System.out.println("Max Price: " + maxPrice);
                    System.out.println("Rating: " + rating);
                    System.out.println("Image: " + image);
                    return d;
                } else {
                    System.out.println("没有找到该CarId的记录！");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Do> UserSelect(String type, String energyType, double minprice, double maxprice, String sortBy, String sortOrder) {
        // 设置默认的价格范围
        if (minprice == -1) minprice = 0;
        if (maxprice == -1) maxprice = Double.MAX_VALUE;

        // 用于存储拼接的表名，避免重复表名
        Set<String> tables = new HashSet<>();
        ArrayList<Do> cars = new ArrayList<>();
        // 根据 type 和 energyType 动态拼接表名
        if (type == null || type.equals("sedan")) {
            if (energyType == null || energyType.equals("oil")) {
                tables.add("car_sedan_oil");
            }
            if (energyType == null || energyType.equals("electric")) {
                tables.add("car_sedan_electric");
            }
        }
        if (type == null || type.equals("mpv")) {
            if (energyType == null || energyType.equals("oil")) {
                tables.add("car_mpv_oil");
            }
            if (energyType == null || energyType.equals("electric")) {
                tables.add("car_mpv_electric");
            }
        }
        if (type == null || type.equals("suv")) {
            if (energyType == null || energyType.equals("oil")) {
                tables.add("car_suv_oil");
            }
            if (energyType == null || energyType.equals("electric")) {
                tables.add("car_suv_electric");
            }
        }

        // 构建查询的表格部分，使用 UNION ALL 连接各表
        StringBuilder sqlBuilder = new StringBuilder("SELECT Car_name, Min_price, Max_price, rating, image FROM (");
        boolean first = true;

        for (String table : tables) {
            if (!first) {
                sqlBuilder.append(" UNION ALL ");
            }
            sqlBuilder.append("SELECT Car_name, Min_price, Max_price, rating, image FROM ").append(table);
            first = false;
        }

        // 完成查询语句
        sqlBuilder.append(") AS car_data WHERE Min_price >= ? AND Max_price <= ?");

        // 如果传入了排序参数，加入 ORDER BY 子句
        if (sortBy != null && !sortBy.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            // 确保 sortBy 的合法性，防止 SQL 注入
            if (sortBy.equals("price")) {
                sqlBuilder.append(" ORDER BY Min_price ").append(sortOrder);
            } else if (sortBy.equals("rating")) {
                sqlBuilder.append(" ORDER BY rating ").append(sortOrder);
            }
        }

        // 打印生成的 SQL 查询，便于调试
        System.out.println("Generated SQL: " + sqlBuilder.toString());

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            // 设置参数
            pstmt.setDouble(1, minprice);
            pstmt.setDouble(2, maxprice);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // 处理查询结果
                    String carName = rs.getString("Car_name");
                    double minPrice = rs.getDouble("Min_price");
                    double maxPrice = rs.getDouble("Max_price");
                    float rating = rs.getFloat("rating");
                    String image = rs.getString("image");
                    System.out.println("Car Name: " + carName);
                    System.out.println("Min Price: " + minPrice);
                    System.out.println("Max Price: " + maxPrice);
                    System.out.println("Rating: " + rating);
                    Do d = new Do(carName, minPrice, maxPrice, rating, image);
                    // ... 其他字段
                    cars.add(d);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }



    // 判断类型是否合法
    private boolean isValidType(String type) {
        return type.equals("suv") || type.equals("sedan") || type.equals("mpv");
    }

    // 判断能源类型是否合法
    private boolean isValidEnergyType(String energyType) {
        return energyType.equals("oil") || energyType.equals("electric");
    }



}
