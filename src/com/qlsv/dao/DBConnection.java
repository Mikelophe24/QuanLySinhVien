package com.qlsv.dao;

import com.qlsv.helper.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp quản lý kết nối tới CSDL MySQL thông qua JDBC.
 * Mỗi lần gọi {@link #getConnection()} trả về một kết nối mới; nơi sử dụng
 * có trách nhiệm đóng kết nối (nên dùng try-with-resources).
 */
public final class DBConnection {

    static {
        try {
            // Nạp driver MySQL (JDBC 4 tự nạp, nhưng gọi tường minh cho chắc chắn)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Không tìm thấy MySQL Connector/J. Hãy thêm file jar vào thư mục lib. "
                    + e.getMessage());
        }
    }

    private DBConnection() {
        // Không cho khởi tạo
    }

    /**
     * Mở một kết nối mới tới CSDL.
     *
     * @return đối tượng {@link Connection}
     * @throws SQLException nếu không kết nối được (sai mật khẩu, MySQL chưa chạy...)
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                ConfigManager.getDbUrl(),
                ConfigManager.getDbUser(),
                ConfigManager.getDbPassword());
    }

    /**
     * Kiểm tra nhanh khả năng kết nối. Dùng khi khởi động ứng dụng để báo lỗi sớm.
     *
     * @return true nếu kết nối thành công
     */
    public static boolean testConnection() {
        try (Connection c = getConnection()) {
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
