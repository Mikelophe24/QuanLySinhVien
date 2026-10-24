package com.qlsv.view;

import com.qlsv.controller.DiemController;
import com.qlsv.controller.LopController;
import com.qlsv.controller.MonHocController;
import com.qlsv.controller.SinhVienController;
import com.qlsv.controller.ThongKeController;
import com.qlsv.model.User;

import javax.swing.*;
import java.awt.*;

/**
 * GIAO DIỆN 2: Giao diện Chính / Dashboard.
 * Dùng {@link JMenuBar} và {@link JToolBar} để điều hướng; vùng trung tâm dùng
 * {@link CardLayout} để chuyển đổi giữa các panel quản lý.
 */
public class MainFrame extends JFrame {

    private final User currentUser;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);

    // Tên định danh cho từng card
    public static final String CARD_HOME = "HOME";
    public static final String CARD_LOP = "LOP";
    public static final String CARD_SINHVIEN = "SINHVIEN";
    public static final String CARD_MONHOC = "MONHOC";
    public static final String CARD_DIEM = "DIEM";
    public static final String CARD_THONGKE = "THONGKE";

    private final JLabel statusBar = new JLabel();

    public MainFrame(User currentUser) {
        this.currentUser = currentUser;

        setTitle("Quản lý Sinh viên - Giao diện chính");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null);

        setJMenuBar(buildMenuBar());
        add(buildToolBar(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        buildCards();
        showCard(CARD_HOME);
    }

    // ------------------------------------------------------------------
    // Thanh menu
    // ------------------------------------------------------------------
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuHeThong = new JMenu("Hệ thống");
        JMenuItem miHome = new JMenuItem("Trang chủ");
        JMenuItem miLogout = new JMenuItem("Đăng xuất");
        JMenuItem miExit = new JMenuItem("Thoát");
        miHome.addActionListener(e -> showCard(CARD_HOME));
        miLogout.addActionListener(e -> logout());
        miExit.addActionListener(e -> System.exit(0));
        menuHeThong.add(miHome);
        menuHeThong.add(miLogout);
        menuHeThong.addSeparator();
        menuHeThong.add(miExit);

        JMenu menuQuanLy = new JMenu("Quản lý");
        JMenuItem miLop = new JMenuItem("Quản lý Lớp");
        JMenuItem miSv = new JMenuItem("Quản lý Sinh viên");
        JMenuItem miMh = new JMenuItem("Quản lý Môn học");
        JMenuItem miDiem = new JMenuItem("Quản lý Điểm");
        miLop.addActionListener(e -> showCard(CARD_LOP));
        miSv.addActionListener(e -> showCard(CARD_SINHVIEN));
        miMh.addActionListener(e -> showCard(CARD_MONHOC));
        miDiem.addActionListener(e -> showCard(CARD_DIEM));
        menuQuanLy.add(miLop);
        menuQuanLy.add(miSv);
        menuQuanLy.add(miMh);
        menuQuanLy.add(miDiem);

        JMenu menuBaoCao = new JMenu("Báo cáo");
        JMenuItem miTk = new JMenuItem("Thống kê / Tìm kiếm");
        miTk.addActionListener(e -> showCard(CARD_THONGKE));
        menuBaoCao.add(miTk);

        JMenu menuTroGiup = new JMenu("Trợ giúp");
        JMenuItem miAbout = new JMenuItem("Giới thiệu");
        miAbout.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Bài tập lớn: Quản lý Sinh viên\nJava Swing + JDBC + MySQL (MVC)",
                "Giới thiệu", JOptionPane.INFORMATION_MESSAGE));
        menuTroGiup.add(miAbout);

        menuBar.add(menuHeThong);
        menuBar.add(menuQuanLy);
        menuBar.add(menuBaoCao);
        menuBar.add(menuTroGiup);
        return menuBar;
    }

    // ------------------------------------------------------------------
    // Thanh công cụ
    // ------------------------------------------------------------------
    private JToolBar buildToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        toolBar.add(makeToolButton("Trang chủ", CARD_HOME));
        toolBar.addSeparator();
        toolBar.add(makeToolButton("Lớp", CARD_LOP));
        toolBar.add(makeToolButton("Sinh viên", CARD_SINHVIEN));
        toolBar.add(makeToolButton("Môn học", CARD_MONHOC));
        toolBar.add(makeToolButton("Điểm", CARD_DIEM));
        toolBar.addSeparator();
        toolBar.add(makeToolButton("Thống kê", CARD_THONGKE));
        return toolBar;
    }

    private JButton makeToolButton(String text, String card) {
        JButton b = new JButton(text);
        b.setFocusable(false);
        b.addActionListener(e -> showCard(card));
        return b;
    }

    // ------------------------------------------------------------------
    // Thanh trạng thái dưới cùng
    // ------------------------------------------------------------------
    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        statusBar.setText("Người dùng: " + currentUser.getHoTen()
                + "  |  Vai trò: " + currentUser.getVaiTro());
        p.add(statusBar, BorderLayout.WEST);
        return p;
    }

    // ------------------------------------------------------------------
    // Khởi tạo các card (panel) và gắn controller
    // ------------------------------------------------------------------
    private void buildCards() {
        content.add(buildHomePanel(), CARD_HOME);

        LopPanel lopPanel = new LopPanel();
        new LopController(lopPanel);
        content.add(lopPanel, CARD_LOP);

        SinhVienPanel svPanel = new SinhVienPanel();
        new SinhVienController(svPanel);
        content.add(svPanel, CARD_SINHVIEN);

        MonHocPanel mhPanel = new MonHocPanel();
        new MonHocController(mhPanel);
        content.add(mhPanel, CARD_MONHOC);

        DiemPanel diemPanel = new DiemPanel();
        new DiemController(diemPanel);
        content.add(diemPanel, CARD_DIEM);

        ThongKePanel tkPanel = new ThongKePanel();
        new ThongKeController(tkPanel);
        content.add(tkPanel, CARD_THONGKE);
    }

    /** Trang chủ với các nút lớn điều hướng. */
    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcome = new JLabel("CHÀO MỪNG ĐẾN HỆ THỐNG QUẢN LÝ SINH VIÊN",
                SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcome.setForeground(new Color(33, 102, 172));
        welcome.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        panel.add(welcome, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 60, 60, 60));
        grid.add(makeBigButton("Quản lý Lớp", CARD_LOP));
        grid.add(makeBigButton("Quản lý Sinh viên", CARD_SINHVIEN));
        grid.add(makeBigButton("Quản lý Môn học", CARD_MONHOC));
        grid.add(makeBigButton("Quản lý Điểm", CARD_DIEM));
        grid.add(makeBigButton("Thống kê / Tìm kiếm", CARD_THONGKE));
        grid.add(makeBigButton("Đăng xuất", null));
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JButton makeBigButton(String text, String card) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setFocusable(false);
        b.setBackground(Color.WHITE);
        if (card == null) {
            b.addActionListener(e -> logout());
        } else {
            b.addActionListener(e -> showCard(card));
        }
        return b;
    }

    private void showCard(String name) {
        cardLayout.show(content, name);
    }

    private void logout() {
        int ok = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame login = new LoginFrame();
                new com.qlsv.controller.LoginController(login);
                login.setVisible(true);
            });
        }
    }
}
