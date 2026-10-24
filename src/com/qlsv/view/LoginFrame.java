package com.qlsv.view;

import javax.swing.*;
import java.awt.*;

/**
 * GIAO DIỆN 1: Đăng nhập / Xác thực.
 * Lớp này chỉ dựng giao diện và cung cấp getter cho các thành phần;
 * phần xử lý sự kiện nằm ở {@link com.qlsv.controller.LoginController}.
 */
public class LoginFrame extends JFrame {

    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnLogin = new JButton("Đăng nhập");
    private final JButton btnExit = new JButton("Thoát");

    public LoginFrame() {
        setTitle("Đăng nhập - Quản lý Sinh viên");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
        setLocationRelativeTo(null); // căn giữa màn hình
    }

    private void buildUI() {
        // Thanh tiêu đề trên cùng
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ SINH VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 102, 172));
        header.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));
        header.add(title, BorderLayout.CENTER);

        // Khu vực nhập liệu
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 15, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("Tên đăng nhập:"), gc);
        gc.gridx = 1;
        form.add(txtUsername, gc);

        gc.gridx = 0; gc.gridy = 1;
        form.add(new JLabel("Mật khẩu:"), gc);
        gc.gridx = 1;
        form.add(txtPassword, gc);

        // Khu vực nút
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnLogin.setPreferredSize(new Dimension(120, 32));
        btnExit.setPreferredSize(new Dimension(100, 32));
        buttons.add(btnLogin);
        buttons.add(btnExit);

        // Gợi ý tài khoản mẫu
        JLabel hint = new JLabel(
                "<html><i>Tài khoản mẫu: admin/admin123 hoặc user/user123</i></html>",
                SwingConstants.CENTER);
        hint.setForeground(Color.GRAY);
        hint.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout());
        south.add(buttons, BorderLayout.CENTER);
        south.add(hint, BorderLayout.SOUTH);
        center.add(south, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);

        // Cho phép nhấn Enter để đăng nhập
        getRootPane().setDefaultButton(btnLogin);
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnExit() {
        return btnExit;
    }
}
