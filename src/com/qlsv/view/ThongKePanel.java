package com.qlsv.view;

import com.qlsv.model.Lop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * GIAO DIỆN 5: Thống kê / Báo cáo / Tìm kiếm nâng cao.
 * Gồm 3 nút thống kê (JOIN + GROUP BY + ORDER BY) và một khu vực tìm kiếm
 * nâng cao kết hợp nhiều điều kiện (tên + lớp + điểm trung bình).
 */
public class ThongKePanel extends JPanel {

    private final JButton btnTkLop = new JButton("SL sinh viên theo lớp");
    private final JButton btnTkDiemSv = new JButton("Điểm TB theo sinh viên");
    private final JButton btnTkDiemMon = new JButton("Điểm TB theo môn học");

    private final JTextField txtTen = new JTextField(12);
    private final JComboBox<Lop> cboLop = new JComboBox<>();
    private final JTextField txtDiemMin = new JTextField(5);
    private final JButton btnTimNangCao = new JButton("Tìm kiếm nâng cao");

    private final JLabel lblTieuDe = new JLabel();
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    public ThongKePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("THỐNG KÊ / TÌM KIẾM NÂNG CAO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(33, 102, 172));

        JPanel north = new JPanel(new BorderLayout(0, 8));
        north.add(title, BorderLayout.NORTH);
        north.add(buildControls(), BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);

        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(6, 2, 6, 2));

        table.getTableHeader().setReorderingAllowed(false);
        JPanel center = new JPanel(new BorderLayout());
        center.add(lblTieuDe, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildControls() {
        JPanel wrap = new JPanel(new GridLayout(2, 1, 0, 6));

        // Hàng 1: các nút thống kê
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row1.setBorder(BorderFactory.createTitledBorder("Thống kê (JOIN + GROUP BY + ORDER BY)"));
        row1.add(btnTkLop);
        row1.add(btnTkDiemSv);
        row1.add(btnTkDiemMon);

        // Hàng 2: tìm kiếm nâng cao
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row2.setBorder(BorderFactory.createTitledBorder("Tìm kiếm nâng cao sinh viên"));
        row2.add(new JLabel("Tên:"));
        row2.add(txtTen);
        row2.add(new JLabel("Lớp:"));
        row2.add(cboLop);
        row2.add(new JLabel("Điểm TB ≥"));
        row2.add(txtDiemMin);
        row2.add(btnTimNangCao);

        wrap.add(row1);
        wrap.add(row2);
        return wrap;
    }

    /** Đặt lại tiêu đề và các cột cho bảng kết quả. */
    public void setColumns(String tieuDe, String[] columns) {
        lblTieuDe.setText(tieuDe);
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(columns);
    }

    public JButton getBtnTkLop() { return btnTkLop; }
    public JButton getBtnTkDiemSv() { return btnTkDiemSv; }
    public JButton getBtnTkDiemMon() { return btnTkDiemMon; }
    public JTextField getTxtTen() { return txtTen; }
    public JComboBox<Lop> getCboLop() { return cboLop; }
    public JTextField getTxtDiemMin() { return txtDiemMin; }
    public JButton getBtnTimNangCao() { return btnTimNangCao; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTable() { return table; }
}
