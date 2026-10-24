package com.qlsv.controller;

import com.qlsv.dao.UserDAO;
import com.qlsv.model.User;
import com.qlsv.view.LoginFrame;
import com.qlsv.view.MainFrame;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Controller cho màn hình đăng nhập.
 * Gắn sự kiện cho nút, gọi {@link UserDAO} để xác thực, và mở {@link MainFrame}
 * khi đăng nhập thành công.
 */
public class LoginController {

    private final LoginFrame view;
    private final UserDAO userDAO = new UserDAO();

    public LoginController(LoginFrame view) {
        this.view = view;
        attachEvents();
    }

    private void attachEvents() {
        view.getBtnLogin().addActionListener(e -> doLogin());
        view.getBtnExit().addActionListener(e -> System.exit(0));
    }

    /** Xử lý khi người dùng nhấn Đăng nhập. */
    private void doLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        // Kiểm tra nhập liệu
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = userDAO.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(view,
                        "Đăng nhập thành công! Xin chào " + user.getHoTen(),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
                // Mở giao diện chính
                SwingUtilities.invokeLater(() -> new MainFrame(user).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(view,
                        "Sai tên đăng nhập hoặc mật khẩu!",
                        "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi kết nối cơ sở dữ liệu:\n" + ex.getMessage(),
                    "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }
}
