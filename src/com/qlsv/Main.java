package com.qlsv;

import com.qlsv.controller.LoginController;
import com.qlsv.dao.DBConnection;
import com.qlsv.view.LoginFrame;

import javax.swing.*;

/**
 * Điểm khởi động của ứng dụng Quản lý Sinh viên.
 * Kiểm tra kết nối CSDL rồi hiển thị màn hình đăng nhập.
 */
public class Main {

    public static void main(String[] args) {
        // Dùng giao diện hệ thống cho đẹp và tự nhiên trên Windows
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {
            // Nếu lỗi thì dùng look-and-feel mặc định
        }

        SwingUtilities.invokeLater(() -> {
            // Kiểm tra kết nối CSDL sớm để báo lỗi rõ ràng
            if (!DBConnection.testConnection()) {
                JOptionPane.showMessageDialog(null,
                        "Không kết nối được tới CSDL MySQL.\n\n"
                        + "Hãy kiểm tra:\n"
                        + "  - MySQL Server đã chạy chưa?\n"
                        + "  - Đã tạo CSDL 'qlsv' (chạy file database/schema.sql) chưa?\n"
                        + "  - Thông tin trong file config.properties (user/mật khẩu) đã đúng chưa?\n"
                        + "  - Đã thêm MySQL Connector/J vào classpath chưa?",
                        "Lỗi kết nối CSDL", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LoginFrame login = new LoginFrame();
            new LoginController(login);
            login.setVisible(true);
        });
    }
}
