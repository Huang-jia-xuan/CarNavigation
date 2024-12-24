package edu.jnu.Operation;

import edu.jnu.entity.Do;
import com.mysql.cj.jdbc.ConnectionImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BasicOperation {

    private static final String URL = "jdbc:mysql://172.20.0.140:3306/cardb";
    private static final String USER = "huangjiaxuan";
    private static final String PASSWORD = "root";




    private Connection getConnection() throws SQLException {
        // 连接数据库的代码（你可以根据你的数据库连接设置修改）
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/cardb", "root", "root");
    }

    public void insertIntoUser(int id, String username, String password) {
        String checkSql = "SELECT 1 FROM admin WHERE id = ? UNION SELECT 1 FROM user WHERE id = ?";
        String insertSql = "INSERT INTO user (id, username, password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // 检查admin和user表中是否已经存在相同的id
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, id);
                checkStmt.setInt(2, id);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("ID " + id + " 已经存在");
                        return; // 退出函数，不插入重复id
                    }
                }
            }

            // 如果没有重复的id，则执行插入操作
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, id);
                insertStmt.setString(2, username);
                insertStmt.setString(3, password);

                // 先插入数据，再创建用户
                insertStmt.executeUpdate();
                System.out.println("User数据插入成功！");

                // 创建 MySQL 用户
                String createUserSql = "CREATE USER ?@'172.20.0.140' IDENTIFIED BY ?";
                try (PreparedStatement createUserStmt = conn.prepareStatement(createUserSql)) {
                    createUserStmt.setString(1, String.valueOf(id));
                    createUserStmt.setString(2, password);

                    createUserStmt.executeUpdate();
                    System.out.println("用户 " + id + " 创建成功");
                }

            }

        } catch (SQLException e) {
            // 处理SQLException，不再抛出
            System.out.println("插入失败: " + e.getMessage());
        }
    }

    // 检查并插入到admin表的函数（不声明 throws SQLException）
    public void insertIntoAdmin(int id, String username, String password) {
        String checkSql = "SELECT 1 FROM admin WHERE id = ? UNION SELECT 1 FROM user WHERE id = ?";
        String insertSql = "INSERT INTO admin (id, username, password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // 检查admin和user表中是否已经存在相同的id
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, id);
                checkStmt.setInt(2, id);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("ID " + id + " 已经存在");
                        return; // 退出函数，不插入重复id
                    }
                }
            }

            // 如果没有重复的id，则执行插入操作
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, id);
                insertStmt.setString(2, username);
                insertStmt.setString(3, password);

                // 先插入数据，再创建用户
                insertStmt.executeUpdate();
                System.out.println("Admin数据插入成功！");

                // 创建 MySQL 用户
                String createUserSql = "CREATE USER ?@'172.20.0.140' IDENTIFIED BY ?";
                try (PreparedStatement createUserStmt = conn.prepareStatement(createUserSql)) {
                    createUserStmt.setString(1, String.valueOf(id));  // 将 id 转为 String 作为用户名
                    createUserStmt.setString(2, password);

                    createUserStmt.executeUpdate();
                    System.out.println("用户 " + id + " 创建成功");
                }

                // 对创建的用户授予所有权限
                String grantSql = "GRANT ALL PRIVILEGES ON *.* TO ?@'172.20.0.140' WITH GRANT OPTION";
                try (PreparedStatement grantStmt = conn.prepareStatement(grantSql)) {
                    grantStmt.setString(1, String.valueOf(id));  // 使用 id 作为用户名

                    grantStmt.executeUpdate();
                    System.out.println("已授予用户 " + id + " 所有权限");
                }

            }

        } catch (SQLException e) {
            // 处理SQLException，不再抛出
            System.out.println("插入失败: " + e.getMessage());
        }
    }
    public String login(int userID, String password) {
        // 构建 SQL 查询，检查 admin 表和 user 表中是否存在该 userID
        String checkUserSql = "SELECT password, 'admin' AS userType FROM admin WHERE id = ? "
                + "UNION SELECT password, 'user' AS userType FROM user WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(checkUserSql)) {

            // 设置参数
            pstmt.setInt(1, userID);
            pstmt.setInt(2, userID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 说明找到了对应的 userID
                    String storedPassword = rs.getString("password");
                    String userType = rs.getString("userType");

                    // 比较输入的密码与数据库中存储的密码
                    if (password.equals(storedPassword)) {
                        return userType;  // 返回 'admin' 或 'user'
                    } else {
                        return "passwordError"; // 密码错误
                    }
                } else {
                    // 如果没有找到对应的 userID
                    return "userNotFound"; // 用户ID不存在
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "dbError"; // 数据库异常
        }
    }


    public static class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
    public void register(int userID, String username, String password, String code) throws UserAlreadyExistsException {
        // 连接数据库
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 1. 检查 userID 是否已存在于 admin 或 user 表中
            String checkSql = "SELECT 1 FROM admin WHERE id = ? UNION SELECT 1 FROM user WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userID);
                checkStmt.setInt(2, userID);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        // 如果 userID 已经存在，抛出异常
                        throw new UserAlreadyExistsException("用户ID " + userID + " 已经存在！");
                    }
                }
            }

            // 2. 根据 code 判断是注册为 admin 还是 user
            boolean isAdmin = false;
            if (code != null && code.matches("admin")) {
                // 如果 code 格式正确，则注册为 admin
                isAdmin = true;
            } else {
                // 否则注册为 user
                if (code != null) {
                    System.out.println("无效的注册码，注册为普通用户！");
                }
            }

            // 3. 调用相应的方法插入数据
            if (isAdmin) {
                // 注册为 admin
                insertIntoAdmin(userID, username, password);
            } else {
                // 注册为 user
                insertIntoUser(userID, username, password);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void grant(int admin_id, int user_id, String option) {
        String checkAdminSql = "SELECT 1 FROM admin WHERE id = ?";
        String grantSql = "GRANT " + option + " ON *.* TO ?@'172.20.0.140'";

        try (Connection conn = getConnection()) {
            // 确认admin_id是否存在
            try (PreparedStatement checkAdminStmt = conn.prepareStatement(checkAdminSql)) {
                checkAdminStmt.setInt(1, admin_id);
                try (ResultSet rs = checkAdminStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Admin ID " + admin_id + " 不存在");
                        return;
                    }
                }
            }

            // 授予权限
            try (PreparedStatement grantStmt = conn.prepareStatement(grantSql)) {
                grantStmt.setString(1, String.valueOf(user_id));
                grantStmt.executeUpdate();
                System.out.println("权限 " + option + " 已授予用户 " + user_id);
            }

        } catch (SQLException e) {
            System.out.println("权限授予失败: " + e.getMessage());
        }
    }

    public void revoke(int admin_id, int user_id, String option) {
        String checkAdminSql = "SELECT 1 FROM admin WHERE id = ?";
        String revokeSql = "REVOKE " + option + " ON *.* FROM ?@'172.20.0.140'";

        try (Connection conn = getConnection()) {
            // 确认admin_id是否存在
            try (PreparedStatement checkAdminStmt = conn.prepareStatement(checkAdminSql)) {
                checkAdminStmt.setInt(1, admin_id);
                try (ResultSet rs = checkAdminStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Admin ID " + admin_id + " 不存在");
                        return;
                    }
                }
            }

            // 撤销权限
            try (PreparedStatement revokeStmt = conn.prepareStatement(revokeSql)) {
                revokeStmt.setString(1, String.valueOf(user_id));
                revokeStmt.executeUpdate();
                System.out.println("权限 " + option + " 已从用户 " + user_id + " 撤销");
            }

        } catch (SQLException e) {
            System.out.println("权限撤销失败: " + e.getMessage());
        }
    }


    public void updateCar(int carid, String carName, Double minPrice, Double maxPrice, Double rating, String image, String type, String energyType, String newtype, String newenergyType, int userId) {
        // 构建表名
        String tableName = "car_" + type + "_" + energyType;

        // 检查用户是否有 UPDATE 权限
        if (!hasUpdatePrivilege(userId)) {
            System.out.println("用户 " + userId + " 没有 UPDATE 权限！");
            return;
        }

        // 1. 获取当前的记录值（包括 carName、minPrice、maxPrice、rating）
        String currentCarName = null;
        double currentMinPrice = -1.0;
        double currentMaxPrice = -1.0;
        double currentRating = -1.0;
        String currentImage = null;

        String selectSQL = "SELECT Car_name, Min_price, Max_price, rating, image FROM " + tableName + " WHERE Carid = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, carid);  // 设置 Carid 条件
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    currentCarName = rs.getString("Car_name");
                    currentMinPrice = rs.getDouble("Min_price");
                    currentMaxPrice = rs.getDouble("Max_price");
                    currentRating = rs.getDouble("rating");
                    currentImage = rs.getString("image");
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

        // 更新字段时，如果字段是 null 或 -1.0，则使用当前值
        if (carName != null) {
            sqlBuilder.append("Car_name = ?");
            first = false;
        }

        if (minPrice != -1.0) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("Min_price = ?");
            first = false;
        }

        if (maxPrice != -1.0) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("Max_price = ?");
            first = false;
        }

        if (rating != -1.0) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("rating = ?");
            first = false;
        }

        if (image != null) {
            if (!first) sqlBuilder.append(", ");
            sqlBuilder.append("image = ?");
        }

        // 添加 WHERE 条件
        sqlBuilder.append(" WHERE Carid = ?");
        String sql = sqlBuilder.toString();
        System.out.println("Generated SQL: " + sql); // 打印生成的 SQL，方便调试

        // 3. 执行更新操作
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;

            // 设置参数，使用传入的值或保持原值
            if (carName != null) {
                pstmt.setString(paramIndex++, carName);
            }

            if (minPrice != -1.0) {
                pstmt.setDouble(paramIndex++, minPrice);
            }

            if (maxPrice != -1.0) {
                pstmt.setDouble(paramIndex++, maxPrice);
            }

            if (rating != -1.0) {
                pstmt.setDouble(paramIndex++, rating);
            }

            if (image != null) {
                pstmt.setString(paramIndex++, image);
            }

            pstmt.setInt(paramIndex, carid); // 最后设置 Carid 条件

            // 执行更新操作
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("更新成功，影响了 " + rowsAffected + " 行。");

            // 如果 newtype 和 newenergyType 不为 null，执行插入和删除操作
            if (newtype != null && newenergyType != null) {
                // 插入更新后的元组到新的表
                insertCar(newtype, newenergyType, carName != null ? carName : currentCarName,
                        minPrice != -1.0 ? minPrice : currentMinPrice,
                        maxPrice != -1.0 ? maxPrice : currentMaxPrice,
                        rating != -1.0 ? rating : currentRating,
                        image != null ? image : currentImage,userId);

                // 删除原记录
                deleteCar(carid, type, energyType,userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // 插入新记录的方法
    public void insertCar(String type, String energyType, String carName, double minPrice, double maxPrice, double rating, String image,int userId) {
        // 构建目标表名
        String tableName = "car_" + type + "_" + energyType;
        if (!hasInsertPrivilege(userId)) {
            System.out.println("用户 " + userId + " 没有插入权限！");
            return;
        }
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
    public void deleteCar(int carId, String type, String energyType,int userId) {
        String tableName = "car_" + type + "_" + energyType;

        if (!hasDeletePrivilege(userId)) {
            System.out.println("用户 " + userId + " 没有删除权限！");
            return;
        }
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


    public Do selectCarById(int carId, String type, String energyType, int userId) {
        // 构建表名
        String tableName = "car_" + type + "_" + energyType;

        // 检查是否是合法的类型和能源类型
        if (!isValidType(type) || !isValidEnergyType(energyType)) {
            System.out.println("无效的类型或能源类型！");
            return null;
        }

        // 检查用户是否具有该表的 SELECT 权限
        if (!hasSelectPrivilege(userId)) {
            System.out.println("用户 " + userId + " 没有权限查询该表！");
            return null;
        }

        // 查询 SQL（去除 EnergyType 字段）
        String sql = "SELECT Carid, Car_name, Min_price, Max_price, rating, image FROM " + tableName + " WHERE Carid = ?";
        System.out.println(sql);  // 打印生成的 SQL，确保正确

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
                    Do d = new Do(carName, minPrice, maxPrice, rating, image);

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



    public static ArrayList<Do> UserSelect(String type, String energyType, double minprice, double maxprice, String sortBy, String sortOrder) {
        // 设置默认的价格范围
        if(minprice>maxprice)
            return null;
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

    public boolean hasSelectPrivilege(int userId) {
        String sql = "SHOW GRANTS FOR ?@'172.20.0.140'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 将 userId 转为字符串作为用户名
            pstmt.setString(1, String.valueOf(userId));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String grant = rs.getString(1);
                    // 检查是否存在对表的 SELECT 权限
                    if (grant.contains("SELECT") ) {
                        return true;  // 用户具有该表的 SELECT 权限
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // 没有找到相应的权限
    }

    public boolean hasInsertPrivilege(int userId) {
        String sql = "SHOW GRANTS FOR ?@'172.20.0.140'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(userId));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String grant = rs.getString(1);
                    if (grant.contains("INSERT")) {
                        return true; // 用户有 INSERT 权限
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasUpdatePrivilege(int userId) {
        String sql = "SHOW GRANTS FOR ?@'172.20.0.140'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(userId));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String grant = rs.getString(1);
                    if (grant.contains("UPDATE")) {
                        return true; // 用户有 UPDATE 权限
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasDeletePrivilege(int userId) {
        String sql = "SHOW GRANTS FOR ?@'172.20.0.140'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(userId));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String grant = rs.getString(1);
                    if (grant.contains("DELETE")) {
                        return true; // 用户有 DELETE 权限
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

