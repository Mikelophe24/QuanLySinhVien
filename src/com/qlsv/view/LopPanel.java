package com.qlsv.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * GIAO DIỆN 3: Quản lý Lớp (Thực thể 1 - Danh mục). Đầy đủ CRUD.
 * Lớp chỉ dựng giao diện và cung cấp getter; logic ở LopController.
 */
public class LopPanel extends JPanel {

    private final JTextField txtMaLop = new JTextField(12);
    private final JTextField txtTenLop = new JTextField(18);
    private final JTextField txtKhoa = new JTextField(18);
    private final JTextField txtNienKhoa = new JTextField(12);

    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnSua = new JButton("Sửa");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnLamMoi = new JButton("Làm mới");

    private final DefaultTableModel tableModel;
    private final JTable table;

    public LopPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTitle(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.WEST);

        tableModel = new DefaultTableModel(
                new String[]{"Mã lớp", "Tên lớp", "Khoa", "Niên khóa"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel("QUẢN LÝ LỚP HỌC", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(33, 102, 172));
        return title;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin lớp"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gc.gridx = 0; gc.gridy = row;  form.add(new JLabel("Mã lớp:"), gc);
        gc.gridx = 1;                  form.add(txtMaLop, gc);
        row++;
        gc.gridx = 0; gc.gridy = row;  form.add(new JLabel("Tên lớp:"), gc);
        gc.gridx = 1;                  form.add(txtTenLop, gc);
        row++;
        gc.gridx = 0; gc.gridy = row;  form.add(new JLabel("Khoa:"), gc);
        gc.gridx = 1;                  form.add(txtKhoa, gc);
        row++;
        gc.gridx = 0; gc.gridy = row;  form.add(new JLabel("Niên khóa:"), gc);
        gc.gridx = 1;                  form.add(txtNienKhoa, gc);
        row++;

        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnLamMoi);
        gc.gridx = 0; gc.gridy = row; gc.gridwidth = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        form.add(buttons, gc);

        return form;
    }

    // ----- Getter cho controller -----
    public JTextField getTxtMaLop() { return txtMaLop; }
    public JTextField getTxtTenLop() { return txtTenLop; }
    public JTextField getTxtKhoa() { return txtKhoa; }
    public JTextField getTxtNienKhoa() { return txtNienKhoa; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
}
