package com.qlsv.dao;

import com.qlsv.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Lớp thao tác dữ liệu cho bảng {@code users}. */
public class UserDAO {

    /**
     * Xác thực đăng nhập.
     *
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return đối tượng {@link User} nếu đúng, {@code null} nếu sai
     * @throws SQLException khi có lỗi truy vấn/kết nối
     */
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, ho_ten, vai_tro "
                   + "FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("ho_ten"),
                            rs.getString("vai_tro"));
                }
            }
        }
        return null;
    }
}
